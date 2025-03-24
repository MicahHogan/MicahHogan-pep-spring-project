package com.example.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.example.entity.Account;

/**
 * Repository interface for accessing and managing Account entities in the database.
 * This interface extends JpaRepository to provide CRUD operations for Account entities.
 *
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    /**
     * Checks if an account with the specified username exists.
     *
     * @param username the username to check
     * @return true if an account with the specified username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Finds an account by its username and password.
     *
     * @param username the username of the account
     * @param password the password of the account
     * @return an Optional containing the found account, or an empty Optional if no account was found
     */
    Optional<Account> findByUsernameAndPassword(String username, String password);
    
    /**
     * Checks if an account with the specified username and password exists.
     *
     * @param username the username to check
     * @param password the password to check
     * @return true if an account with the specified username and password exists, false otherwise
     */
    boolean existsByUsernameAndPassword(String username, String password);
    
    /**
     * Deletes an account by its ID.
     * This method overrides the default implementation to ensure non-null ID values.
     *
     * @param accountId the ID of the account to delete
     * @throws IllegalArgumentException if accountId is null
     */
    void deleteById(@NonNull Integer accountId);
}

