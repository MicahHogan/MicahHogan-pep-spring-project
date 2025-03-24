package com.example.exception;

/**
 * Thrown when a data integrity constraint has been violated.
 * 
 * <p>This exception indicates that an operation would result in corrupt data, inconsistent
 * state, or violation of data integrity rules. It is typically thrown during data
 * manipulation operations when constraints beyond simple validation rules are violated.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>Attempting to delete a parent record that has dependent child records</li>
 *   <li>Violating a database foreign key constraint</li>
 *   <li>Breaking referential integrity between related entities</li>
 *   <li>Operations that would leave data in an inconsistent or corrupted state</li>
 * </ul>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class DataIntegrityException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public DataIntegrityException(String message) {
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
    public DataIntegrityException(String message, Exception exception) {
        super(message, exception);
    }
}

