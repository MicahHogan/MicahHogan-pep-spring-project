package com.example.exception;

/**
 * Thrown when a client sends a request that is malformed or invalid.
 * 
 * <p>This exception indicates that the server cannot or will not process the request
 * due to something that is perceived to be a client error. It is typically thrown
 * during request processing when the structure, format, or content of the request
 * does not meet the requirements for processing.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>Missing required request parameters or headers</li>
 *   <li>Invalid JSON or XML structure in the request body</li>
 *   <li>Incompatible content type in the request</li>
 *   <li>Logical inconsistencies in the request parameters</li>
 * </ul>
 * 
 * <p>This exception typically results in a 400 Bad Request HTTP response.</p>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public BadRequestException(String message) {
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
    public BadRequestException(String message, Throwable exception) {
        super(message, exception);
    }
}

