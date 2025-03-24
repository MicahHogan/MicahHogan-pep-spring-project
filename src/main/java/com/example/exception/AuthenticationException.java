package com.example.exception;

/**
 * Thrown when authentication fails for a user or client.
 * 
 * <p>This exception indicates that the provided credentials or authentication
 * tokens are invalid, expired, or otherwise insufficient to establish identity.
 * It is typically thrown during login processes, token validation, or when
 * accessing protected resources.</p>
 * 
 * <p>Examples of scenarios where this exception might be thrown include:</p>
 * <ul>
 *   <li>Invalid username and password combination</li>
 *   <li>Expired authentication token or session</li>
 *   <li>Invalid API key or OAuth token</li>
 *   <li>Account locked or disabled</li>
 * </ul>
 * 
 * <p>This exception typically results in a 401 Unauthorized HTTP response.</p>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public AuthenticationException(String message) {
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
    public AuthenticationException(String message, Throwable exception) {
        super(message, exception);
    }
}

