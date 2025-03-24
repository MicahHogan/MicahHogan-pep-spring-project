package com.example.exception;

/**
 * Thrown when an attempt is made to create a resource that already exists.
 * 
 * <p>This exception indicates that the application detected a uniqueness constraint
 * violation. It is typically thrown during creation operations when a resource with
 * the same unique identifier or unique properties already exists in the system.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>Registering a user with an email that's already in use</li>
 *   <li>Creating a product with a SKU that already exists</li>
 *   <li>Adding a category with a name that must be unique</li>
 *   <li>Any operation that would violate a database's unique constraint</li>
 * </ul>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public DuplicateResourceException(String message) {
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
    public DuplicateResourceException(String message, Throwable exception) {
        super(message, exception);
    }
}

