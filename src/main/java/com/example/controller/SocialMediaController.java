package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * REST Controller for the Social Media application that handles all REST API endpoints.
 * This controller provides CRUD operations for accounts and messages, as well as
 * authentication functionality such as login and registration.
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 2025-03-23
 */
@RestController
@RequestMapping("/")
public class SocialMediaController {

    /**
     * Logger for the SocialMediaController class to log information, warnings, and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SocialMediaController.class);

    /**
     * Service layer component that handles business logic related to Account entities.
     */
    @Autowired
    private AccountService accountService;
    
    /**
     * Service layer component that handles business logic related to Message entities.
     */
    @Autowired
    private MessageService messageService;

    /**
     * Handles the root endpoint request and returns a welcome message.
     * 
     * @return ResponseEntity containing an ApiResponse with a welcome message
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> home() {
        ApiResponse<String> response = new ApiResponse<>(null, "Welcome to the Social Media API!", true);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user registration requests by creating a new account in the system.
     * 
     * @param account The account object containing username and password to be registered
     * @return ResponseEntity containing the registered Account object with its generated ID
     */
    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account) {
        LOGGER.info("Received request to register a new user.");
        account = accountService.save(account);
        return ResponseEntity.ok().body(account);
    }
    
    /**
     * Handles user login requests by validating credentials against the database.
     * 
     * @param account The account object containing username and password to be validated
     * @return ResponseEntity containing the Account object if login is successful, 
     *         or a 401 Unauthorized status if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        boolean accountExists = accountService.existsByUsernameAndPassword(account);
        
        if (accountExists) {
            accountService.loginAccount(account);
            Account loggedInAccount = accountService.findByUsernameAndPassword(account);
            return ResponseEntity.ok().body(loggedInAccount);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    
    
    /**
     * Creates a new message in the system.
     * 
     * @param message The message object to be created
     * @return ResponseEntity containing the created Message object with its generated ID,
     *         or a 400 Bad Request status if the message is invalid
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.postMessage(message);

        if (createdMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
    }

    /**
     * Retrieves all messages from the system.
     * 
     * @return List of all Message objects in the system
     */
    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.getAllMessages();
    }
    
    /**
     * Retrieves a specific message by its ID.
     * 
     * @param messageId The ID of the message to retrieve
     * @return ResponseEntity containing the Message object if found, or null if not found
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> messageByIdOptional = messageService.getById(messageId);

        if (messageByIdOptional.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }

        Message messageById = messageByIdOptional.get();
        return ResponseEntity.ok(messageById);
    }

    /**
     * Deletes a message by its ID.
     * 
     * @param messageId The ID of the message to delete
     * @return ResponseEntity containing the number of rows affected by the delete operation,
     *         or null if the message was not found
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        Optional<Message> messageByIdOptional = messageService.getById(messageId);

        if (messageByIdOptional.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }

        int rowsAffected = messageService.deleteById(messageId);
        return ResponseEntity.ok().body(rowsAffected);
    }

    /**
     * Updates the text content of an existing message.
     * 
     * @param messageId The ID of the message to update
     * @param messageRequest The message object containing the new message text
     * @return ResponseEntity containing the number of rows affected by the update operation,
     *         or a 400 Bad Request status if the update was unsuccessful
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message messageRequest) {
        String updatedMessageText = messageRequest.getMessageText();
        int rowsAffected = messageService.updateMessage(messageId, updatedMessageText);

        if (rowsAffected == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        return ResponseEntity.ok().body(rowsAffected);
    }

    /**
     * Retrieves all user accounts from the system.
     * 
     * @return ResponseEntity containing a list of all Account objects in the system
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllUsers() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    /**
     * Retrieves all messages posted by a specific user.
     * 
     * @param accountId The ID of the user account whose messages to retrieve
     * @return ResponseEntity containing a list of Message objects posted by the specified user
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messagesByUser = messageService.getMessagesByUserId(accountId);
        return ResponseEntity.ok(messagesByUser);
    }

    /**
     * Deletes a user account by its ID.
     * 
     * @param accountId The ID of the user account to delete
     * @return ResponseEntity containing a message indicating success or failure,
     *         with appropriate HTTP status codes
     */
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer accountId) {
        Map<String, String> response = new HashMap<>();
        boolean isDeleted = accountService.deleteById(accountId);
        
        if (isDeleted) {
            response.put("message", "User deleted successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
