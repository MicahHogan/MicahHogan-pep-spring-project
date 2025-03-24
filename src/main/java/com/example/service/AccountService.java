package com.example.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Account;
import com.example.exception.AuthenticationException;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateResourceException;
import com.example.exception.InvalidInputException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;

/**
 * Service class that provides business logic for managing Account entities.
 * This service handles account creation, authentication, validation, and management operations.
 * All methods include appropriate validation and error handling.
 *
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
@Transactional
@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a new account after validating the input data.
     * 
     * @param account the account to be created
     * @return the created account with generated ID
     * @throws InvalidInputException if the account is null, username is null/blank, or password is too short
     * @throws DuplicateResourceException if an account with the same username already exists
     */
    public Account createAccount(Account account) {
        LOGGER.info("Received request to create a new account -  AccountService.createAccount(Account account) method.");

        if (account == null) {
            LOGGER.error("Account is null. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Account is null. Account creation failed.");
        }

        if (account.getUsername() == null) {
            LOGGER.error("Username is null. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Username is null. Account creation failed.");
        }

        if (account.getUsername().trim().isBlank()) {
            LOGGER.error("Username is blank. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Username is blank. Account creation failed.");
        }

        if (account.getPassword() == null) {
            LOGGER.error("Password is blank. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Password is null. Account creation failed.");
        }

        if (account.getPassword().trim().isBlank()) {
            LOGGER.error("Password is blank. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Password is blank. Account creation failed.");
        }

        if (account.getPassword().length() < 4) {
            LOGGER.error("Password is too short. It must be at least 4 characters. Account creation failed - AccountService.createAccount(Account account) method.");
            throw new InvalidInputException("Password is too short. It must be at least 4 characters. Account creation failed.");
        }

        if (accountRepository.existsByUsername(account.getUsername())) {
            LOGGER.error("An account for the same user: {} already exists - Account creation failed in AccountRepository layer - AccountService.createAccount(Account account) method.", account.getUsername());
            throw new DuplicateResourceException("An account with the same username: " + account.getUsername()+ " already exists - Account creation failed.");
        }
        
        account = accountRepository.save(account);
        LOGGER.info("Successfully created account for user: {} - AccountService.createAccount(Account account) method.", account.getUsername());
        return account;
    }

    /**
     * Authenticates an account based on username and password.
     * 
     * @param account the account containing login credentials
     * @return the authenticated account
     * @throws InvalidInputException if account, username, or password is null/blank
     * @throws AuthenticationException if authentication fails
     */
    @Transactional(readOnly = true)
    public Account loginAccount(Account account) {

        if (account == null) {
            LOGGER.error("Account object is null. Authentication failed - AccountService.loginAccount(Account account) method.");
            throw new InvalidInputException("Account is null. Account creation failed.");
        }

        if (account.getUsername() == null) {
            LOGGER.error("Account username is null. Authentication failed - AccountService.loginAccount(Account account) method.");
            throw new InvalidInputException("Account username is null. Account creation failed.");
        }

        if (account.getUsername().trim().isBlank()) {
            LOGGER.error("Account username is blank. Authentication failed - AccountService.loginAccount(Account account) method.");
            throw new InvalidInputException("Account username is blank. Account creation failed.");
        }

        if (account.getPassword() == null) {
            LOGGER.error("Account password is null. Authentication failed - AccountService.loginAccount(Account account) method.");
            throw new InvalidInputException("Account password is null. Account creation failed.");
        }

        if (account.getPassword().trim().isBlank()) {
            LOGGER.error("Account password is blank. Authentication failed - AccountService.loginAccount(Account account) method.");
            throw new InvalidInputException("Account password is blank. Account creation failed.");
        }

        LOGGER.info("Received request to authenticate account for user: {} -  AccountService.loginAccount(Account account) method.", account.getUsername());

        Optional<Account> loggedInAccountOptional = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());

        if (loggedInAccountOptional.isEmpty()) {
            LOGGER.error("Authentication failed for account with username: {} - AccountService.loginAccount(Account account) method.", account.getUsername());
            throw new AuthenticationException("Authentication failed for account with username: " + account.getUsername() + ".");
        }

        LOGGER.info("Successfully authenticated account with username: {} - AccountService.loginAccount(Account account) method.", account.getUsername());
        return loggedInAccountOptional.get();
    }

     /**
     * Check if a username already exists in the system.
     * 
     * @param username The username to check
     * @return true if the username exists, false otherwise
     * @throws InvalidInputException if username is null or blank
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        if (username == null) {
            LOGGER.error("Username is null. Null user does not exist - AccountService.usernameExists(String username) method.");
            throw new InvalidInputException("Username is null. Null user does not exist.");
        }

        if (username.trim().isBlank()) {
            LOGGER.error("Username is blank. Blank user does not exist - AccountService.usernameExists(String username) method.");
            throw new InvalidInputException("Account username is blank. Blank user does not exist - AccountService.usernameExists(String username) method.");
        }

        LOGGER.info("Received request to check if user: {} exists - AccountService.usernameExists(String username) method.", username);
        boolean userExists = accountRepository.existsByUsername(username);
        if (!userExists) {
            LOGGER.warn("User: {} does not exist - AccountService.usernameExists(String username) method.", username);
            return userExists;
        }
        LOGGER.info("User: {} exists - AccountService.usernameExists(String username) method.",username);
        return userExists;
    }

    /**
     * Checks if an account with the specified username and password exists.
     * 
     * @param account the account containing username and password to check
     * @return true if an account with the specified username and password exists, false otherwise
     * @throws InvalidInputException if account, username, or password is null/blank
     */
    @Transactional(readOnly = true)
    public boolean existsByUsernameAndPassword(Account account) {

        if (account == null) {
            LOGGER.error("Account object is null. Checking if account exists failed - AccountService.existsByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account is null. Checking if account exists failed.");
        }

        if (account.getUsername() == null) {
            LOGGER.error("Username is null. Checking if account exists failed - AccountService.existsByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Username is null. Null user does not exist.");
        }

        if (account.getPassword() == null) {
            LOGGER.error("Password is null. Checking if account exists failed - AccountService.existsByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Password is null. User with null password does not exist.");
        }

        if (account.getUsername().trim().isBlank()) {
            LOGGER.error("Username is blank. Blank user does not exist - AccountService.existsByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Username is blank. Blank user does not exist.");
        }

        if (account.getPassword().trim().isBlank()) {
            LOGGER.error("Password is blank. User with blank password does not exist - AccountService.existsByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Password is blank. User with blank password does not exist.");
        }

        String username = account.getUsername();
        String password = account.getPassword();


        LOGGER.info("Received request to check if user: {} exists - AccountService.usernameExists(String username) method.", account.getUsername());
        boolean userExists = accountRepository.existsByUsernameAndPassword(username, password);
        if (!userExists) {
            LOGGER.warn("User: {} does not exist - AccountService.usernameExists(String username) method.", username);
            return userExists;
        }
        LOGGER.info("User: {} exists - AccountService.usernameExists(String username) method.", username);
        return userExists;
    }

    /**
     * Finds an account by username and password.
     * 
     * @param account the account containing username and password to search for
     * @return the found account
     * @throws InvalidInputException if account, username, or password is null/blank
     * @throws BadRequestException if no account is found with the given username and password
     */
    @Transactional(readOnly = true)
    public Account findByUsernameAndPassword(Account account) {

        if (account == null) {
            LOGGER.error("Account object is null. Search for user failed - AccountService.findByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account object is null. Search for user failed.");
        }

        if (account.getUsername() == null) {
            LOGGER.error("Account username is null. Search for user failed - AccountService.findByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account username is null. Search for user failed.");
        }

        if (account.getUsername().trim().isBlank()) {
            LOGGER.error("Account username is blank. Search for user failed - AccountService.findByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account username is blank. Search for user failed.");
        }

        if (account.getPassword() == null) {
            LOGGER.error("Account password is null. Search for user failed - AccountService.findByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account password is null. Search for user failed.");
        }

        if (account.getPassword().trim().isBlank()) {
            LOGGER.error("Account password is blank. Search for user failed - AccountService.findByUsernameAndPassword(Account account) method.");
            throw new InvalidInputException("Account password is blank. Search for user failed.");
        }
        LOGGER.info("Received request to find user: {} by username and password - AccountService.findByUsernameAndPassword(Account account) method.", account.getUsername());
        Optional<Account> foundAccountOptional = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (foundAccountOptional.isEmpty()) {
            LOGGER.error("Search for user: {} failed - AccountService.findByUsername(Account account) method.", account.getUsername());
            throw new BadRequestException("Search for user: " + account.getUsername() + " failed.");
        }
        
        LOGGER.info("Successfully found user: {} - AccountService.findByUsername(Account account) method.", foundAccountOptional.get().getUsername());
        return foundAccountOptional.get();
    }

    /**
     * Saves a new account to the database after validating the input.
     * 
     * @param account the account to save
     * @return the saved account with generated ID
     * @throws InvalidInputException if account, username, or password is null/blank
     * @throws DuplicateResourceException if an account with the same username already exists
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Account save(Account account) {
        if (account == null) {
            LOGGER.error("Account object is null. Saving account failed - AccountService.save(Account account) method.");
            throw new InvalidInputException("Account is null. Saving account failed.");
        }
        if (account.getUsername() == null) {
            LOGGER.error("Account username is null. Saving account failed - AccountService.save(Account account) method.");
            throw new InvalidInputException("Account username is null. Saving account failed.");
        }
        if (account.getPassword() == null) {
            LOGGER.error("Account password is null. Saving account failed - AccountService.save(Account account) method.");
            throw new InvalidInputException("Account password is null. Saving account failed.");
        }
        if (account.getUsername().trim().isBlank()) {
            LOGGER.error("Account username is blank. Saving account failed - AccountService.save(Account account) method.");
            throw new InvalidInputException("Account username is blank. Saving account failed.");
        }
        if (account.getPassword().trim().isBlank()) {
            LOGGER.error("Account password is blank. Saving account failed - AccountService.save(Account account) method.");
            throw new InvalidInputException("Account password is blank. Saving account failed.");
        }
        LOGGER.info("Received request to save user: {} account to database - - AccountService.save(Account account) method.", account.getUsername());
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new DuplicateResourceException("User: " + account.getUsername() + " already exists.");
        }
        LOGGER.info("Successfully saved user: {} account - AccountService.save(Account account) method.", account.getUsername());
        return accountRepository.save(account);
    }

    /**
     * Retrieves all accounts from the database.
     * 
     * @return a list of all accounts
     */
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Retrieves an account by its ID.
     * 
     * @param accountId the ID of the account to retrieve
     * @return the account with the specified ID
     * @throws ResourceNotFoundException if no account exists with the given ID
     */
    @Transactional(readOnly = true)
    public Account getById(Integer accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("User with ID: " + accountId + " not found.");
        }
        return accountOptional.get();
    }

    /**
     * Deletes an account by its ID.
     * 
     * @param accountId the ID of the account to delete
     * @return true if the account was successfully deleted, false if no account with the given ID exists
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public boolean deleteById(Integer accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }
    
}
