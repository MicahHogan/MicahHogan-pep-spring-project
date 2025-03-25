package com.example.controller;

import java.util.Objects;

/**
 * A generic response wrapper for API endpoints.
 * <p>
 * This class provides a standardized structure for API responses,
 * containing the actual data, a message, and a success indicator.
 * </p>
 * 
 * @param <T> The type of data being returned in the response
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    /**
     * Constructs a complete API response with all fields.
     *
     * @param data The payload to be returned
     * @param message A descriptive message about the response
     * @param success Indicates whether the operation was successful
     */
    public ApiResponse(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    /**
     * Constructs an API response with data only.
     * <p>
     * This constructor assumes the operation was successful.
     * </p>
     *
     * @param data The payload to be returned
     */
    public ApiResponse(T data) {
        this.data = data;
        this.success = true; // Assuming success by default
    }

    /**
     * Constructs an API response with a message only.
     * <p>
     * This constructor is typically used for error responses
     * and assumes the operation failed.
     * </p>
     *
     * @param message A descriptive error message
     */
    public ApiResponse(String message) {
        this.message = message;
        this.success = false; // Assuming failure if only message is passed
    }

    /**
     * Constructs an API response with only the success status.
     *
     * @param success Indicates whether the operation was successful
     */
    public ApiResponse(boolean success) {
        this.success = success;
    }

    /**
     * Retrieves the data payload of the response.
     *
     * @return The data contained in the response
     */
    public T getData() {
        return data;
    }

    /**
     * Retrieves the message from the response.
     *
     * @return The descriptive message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks if the operation was successful.
     *
     * @return {@code true} if successful, {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the data payload for the response.
     *
     * @param data The data to be set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Sets the descriptive message for the response.
     *
     * @param message The message to be set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the success status for the response.
     *
     * @param success The success status to be set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Compares this ApiResponse object with another object for equality.
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
        if (!(object instanceof ApiResponse)) {
            return false;
        }
        ApiResponse<?> apiResponse = (ApiResponse<?>) object;
        return Objects.equals(this.data, apiResponse.getData()) &&
            Objects.equals(this.message, apiResponse.getMessage()) &&
            this.success == apiResponse.isSuccess();
    }

    /**
     * Generates the hash code for this ApiResponse object.
     * 
     * @return the hash code of the ApiResponse
     */
    @Override
    public int hashCode() {
        return Objects.hash(data, message, success);
    }

    /**
     * Returns a string representation of the ApiResponse object.
     * This includes the object's data, message, and success
     * in a structured format.
     *
     * @return a string representation of the ApiError object.
     */
    @Override
    public String toString() {
        return "ApiResponse{data = " + data.toString() +
            ", message = '" + message +
            "', success = " + success +
            "}";
    }
}
