package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.InvalidInputException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

/**
 * Service class that handles business logic for Message entities.
 * Provides methods for creating, retrieving, updating, and deleting messages.
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
@Transactional
@Service
public class MessageService {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    /**
     * Maximum allowed length for message text.
     */
    private static final int MAX_MESSAGE_LENGTH = 255;

    /**
     * Value representing number of rows in database affected to be returned in successful
     * message update.
     */
    private static final int ROWS_AFFECTED = 1;

    /**
     * Repository for message data access.
     */
    @Autowired
    private MessageRepository messageRepository;

    /**
     * Repository for account data access.
     */
    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a new message in the system.
     * 
     * @param message The message to be posted
     * @return The saved message with generated ID
     * @throws InvalidInputException if the message text is null, blank, or exceeds maximum length
     * @throws ResourceNotFoundException if the user posting the message doesn't exist
     */
    public Message postMessage(Message message) {
        LOGGER.info("Received request to post a new message - MessageService.postMessage(Message message) method.");

        if (message == null) {
            LOGGER.error("Message object is null. Message creation failed - MessageService.postMessage(Message message) method.");
            throw new InvalidInputException("Message object is null. Message creation failed.");
        }

        if (message.getMessageText() == null || message.getMessageText().isBlank()) {
            LOGGER.error("Message text is null or blank. Message creation failed - MessageService.postMessage(Message message) method.");
            throw new InvalidInputException("Message text cannot be empty. Message creation failed.");
        }

        if (message.getMessageText().length() > MAX_MESSAGE_LENGTH) {
            LOGGER.error("Message text exceeds maximum length of {} characters. Message creation failed - MessageService.postMessage(Message message) method.", MAX_MESSAGE_LENGTH);
            throw new InvalidInputException("Message text exceeds maximum length of " + MAX_MESSAGE_LENGTH + " characters. Message creation failed.");
        }

        if (message.getPostedBy() == null) {
            LOGGER.error("User ID is null. Message creation failed - MessageService.postMessage(Message message) method.");
            throw new InvalidInputException("User ID cannot be null. Message creation failed.");
        }

        if (!accountRepository.existsById(message.getPostedBy())) {
            LOGGER.error("User with ID {} does not exist. Message creation failed - MessageService.postMessage(Message message) method.", message.getPostedBy());
            throw new BadRequestException("User with ID " + message.getPostedBy() + " does not exist. Message creation failed.");
        }

        Message savedMessage = messageRepository.save(message);
        LOGGER.info("Successfully created message with ID: {} - MessageService.postMessage(Message message) method.", savedMessage.getMessageId());
        return savedMessage;
    }

    /**
     * Retrieves all messages in the system.
     * 
     * @return A list of all messages
     */
    @Transactional(readOnly = true)
    public List<Message> getAllMessages() {
        LOGGER.info("Received request to get all messages - MessageService.getAllMessages() method.");
        List<Message> messages = messageRepository.findAll();
        LOGGER.info("Successfully retrieved {} messages - MessageService.getAllMessages() method.", messages.size());
        return messages;
    }

    /**
     * Retrieves a message by its ID.
     * 
     * @param messageId The ID of the message to retrieve
     * @return An Optional containing the message if found, empty otherwise
     * @throws InvalidInputException if the messageId is null
     */
    @Transactional(readOnly = true)
    public Optional<Message> getById(Integer messageId) {
        LOGGER.info("Received request to get message with ID: {} - MessageService.getById(Integer messageId) method.", messageId);
        
        if (messageId == null) {
            LOGGER.error("Message ID is null. Message retrieval failed - MessageService.getById(Integer messageId) method.");
            throw new InvalidInputException("Message ID cannot be null. Message retrieval failed.");
        }
        
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent()) {
            LOGGER.info("Successfully retrieved message with ID: {} - MessageService.getById(Integer messageId) method.", messageId);
        } else {
            LOGGER.info("No message found with ID: {} - MessageService.getById(Integer messageId) method.", messageId);
        }
        return message;
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId The ID of the message to delete
     * @return 1, the number of rows in the database affected if deletion was successful
     * @throws BadRequestException if the message ID provided does not belong to a valid message
     * @throws InvalidInputException if the messageId is null
     */
    public int deleteById(Integer messageId) {
        LOGGER.info("Received request to delete message with ID: {} - MessageService.deleteById(Integer messageId) method.", messageId);

        if (messageId == null) {
            LOGGER.error("Message ID is null. Message deletion failed - MessageService.deleteById(Integer messageId) method.");
            throw new InvalidInputException("Message ID cannot be null. Message deletion failed.");
        }

        if (!messageRepository.existsById(messageId)) {
            LOGGER.info("No message found with ID: {} - MessageService.deleteById(Integer messageId) method.", messageId);
            throw new BadRequestException("Message with ID " + messageId + " not found. Message deletion failed.");
            
        }
        messageRepository.deleteById(messageId);
        LOGGER.info("Successfully deleted message with ID: {} - MessageService.deleteById(Integer messageId) method.", messageId);
        return ROWS_AFFECTED;
         
    }

    /**
     * Updates a message's text.
     * 
     * @param messageId The ID of the message to update
     * @param updatedMessageText The new text for the message
     * @return 1 if update was successful
     * @throws InvalidInputException if the messageId is null, text is blank or too long
     * @throws BadRequestException if the message doesn't exist
     */
    public int updateMessage(Integer messageId, String updatedMessageText) {
        LOGGER.info("Received request to update message with ID: {} - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.", messageId);

        if (messageId == null) {
            LOGGER.error("Message ID is null. Message update failed - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.");
            throw new InvalidInputException("Message ID cannot be null. Message update failed.");
        }

        if (updatedMessageText == null || updatedMessageText.trim().isEmpty()) {
            LOGGER.error("Message text is null or blank. Message update failed - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.");
            throw new InvalidInputException("Message text cannot be empty. Message update failed.");
        }

        if (updatedMessageText.length() > MAX_MESSAGE_LENGTH) {
            LOGGER.error("Message text exceeds maximum length of {} characters. Message update failed - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.", MAX_MESSAGE_LENGTH);
            throw new InvalidInputException("Message text exceeds maximum length of " + MAX_MESSAGE_LENGTH + " characters. Message update failed.");
        }

        Optional<Message> validMessageOptional = messageRepository.findById(messageId);

        if (validMessageOptional.isEmpty()) {
            LOGGER.error("No message found with ID: {}. Message update failed - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.", messageId);
            throw new BadRequestException("Message with ID " + messageId + " not found. Message update failed."); 
             
        }
        Message validMessage = validMessageOptional.get();
        validMessage.setMessageText(updatedMessageText);
        messageRepository.save(validMessage);
        LOGGER.info("Successfully updated message with ID: {} - MessageService.updateMessage(Integer messageId, String updatedMessageText) method.", messageId);

        return ROWS_AFFECTED;  
    }

    /**
     * Retrieves all messages posted by a specific user.
     * 
     * @param accountId The ID of the user whose messages to retrieve
     * @return A list of messages posted by the specified user
     * @throws InvalidInputException if the accountId is null
     * @throws ResourceNotFoundException if the user doesn't exist
     */
    @Transactional(readOnly = true)
    public List<Message> getMessagesByUserId(Integer accountId) {
        LOGGER.info("Received request to get messages for user with ID: {} - MessageService.getMessagesByUserId(Integer accountId) method.", accountId);

        if (accountId == null) {
            LOGGER.error("Account ID is null. Message retrieval failed - MessageService.getMessagesByUserId(Integer accountId) method.");
            throw new InvalidInputException("Account ID cannot be null. Message retrieval failed.");
        }

        Optional<Account> validAccountOptional = accountRepository.findById(accountId);

        if (validAccountOptional.isEmpty()) {
            LOGGER.error("User with ID {} does not exist. Message retrieval failed - MessageService.getMessagesByUserId(Integer accountId) method.", accountId);
            throw new ResourceNotFoundException("User with ID " + accountId + " does not exist. Message retrieval failed.");
        }

        List<Message> messages = messageRepository.findByPostedBy(accountId);
        LOGGER.info("Successfully retrieved {} messages for user with ID: {} - MessageService.getMessagesByUserId(Integer accountId) method.", messages.size(), accountId);
        return messages;
    }
}
