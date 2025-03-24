package com.example.exception;

/**
 * Thrown when a requested resource cannot be found in the system.
 * 
 * <p>This exception is typically thrown during data retrieval operations when
 * an entity identified by a specific ID, unique identifier, or other search criteria
 * does not exist in the database or repository.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>User requests a profile that doesn't exist</li>
 *   <li>Client tries to update an entity that has been deleted</li>
 *   <li>Search operation returns no results when at least one is expected</li>
 * </ul>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * <p>Note that the detail message associated with {@code exception} is
     * not automatically incorporated in this exception's detail message.</p>
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     * @param exception the cause (which is saved for later retrieval by the
     *        {@link #getCause()} method). A {@code null} value is permitted,
     *        and indicates that the cause is nonexistent or unknown.
     */
    public ResourceNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }
}

