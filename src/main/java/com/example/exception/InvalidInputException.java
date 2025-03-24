package com.example.exception;

/**
 * Thrown when input provided by a user or client is invalid or malformed.
 * 
 * <p>This exception indicates that the application received input that does not
 * conform to expected formats, types, or validation rules. It is typically thrown
 * during input validation in controllers, services, or data processing layers.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>Malformed email address in a registration form</li>
 *   <li>Invalid date format in a request parameter</li>
 *   <li>Out-of-range values for numeric fields</li>
 *   <li>String length or pattern violations</li>
 * </ul>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class InvalidInputException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public InvalidInputException(String message) {
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
    public InvalidInputException(String message, Exception exception) {
        super(message, exception);
    }
}
