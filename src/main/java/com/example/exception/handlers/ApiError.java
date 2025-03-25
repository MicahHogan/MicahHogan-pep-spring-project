package com.example.exception.handlers;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents an API error response that can be returned to clients.
 * 
 * <p>This class encapsulates error information including HTTP status code, 
 * error message, and the timestamp when the error occurred. It is typically
 * used by exception handlers to provide standardized error responses.</p>
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class ApiError {
    
    /**
     * The HTTP status code associated with this error.
     */
    private int status;
    
    /**
     * A descriptive message explaining the error.
     */
    private String message;
    
    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;
    
    /**
     * Constructs a new ApiError with the specified status and message.
     * 
     * <p>The timestamp is automatically set to the current time when
     * this constructor is called.</p>
     * 
     * @param status  the HTTP status code, such as 400, 404, or 500
     * @param message a descriptive message explaining the error
     */
    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Returns the HTTP status code associated with this error.
     * 
     * @return the HTTP status code
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Returns the descriptive message explaining the error.
     * 
     * @return the error message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Returns the timestamp when the error occurred.
     * 
     * @return the timestamp as a LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Sets the HTTP status code for this error.
     * 
     * @param status the HTTP status code to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
    /**
     * Sets the descriptive message for this error.
     * 
     * @param message the error message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Sets the timestamp for this error.
     * 
     * @param timestamp the LocalDateTime to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Compares this ApiError object with another object for equality.
     * 
     * @param object the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof ApiError)) {
            return false;
        }
        ApiError apiError = (ApiError) object;
        return status == apiError.getStatus() &&
           Objects.equals(this.message, apiError.getMessage()) &&
           Objects.equals(this.timestamp, apiError.getTimestamp());
}

    /**
     * Generates the hash code for this ApiError object.
     * 
     * @return the hash code of the ApiError
     */
    @Override
    public int hashCode() {
        return Objects.hash(status, message, timestamp);
    }

     /**
     * Returns a string representation of the ApiError object.
     * This includes the object's status, message, and timestamp
     * in a structured format.
     *
     * @return a string representation of the ApiError object.
     */
    @Override
    public String toString() {
        return "ApiError{status = " + status +
            ", message = '" + message +
            "', timestamp = " + timestamp +
            "}";
    }
}
