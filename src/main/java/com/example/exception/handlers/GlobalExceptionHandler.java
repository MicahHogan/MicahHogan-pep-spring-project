package com.example.exception.handlers;

import com.example.exception.*;

import javax.persistence.PersistenceException;
import org.springframework.dao.QueryTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * GlobalExceptionHandler is a controller advice that provides centralized exception handling
 * across the entire application. It intercepts exceptions thrown by controllers and returns appropriate 
 * HTTP responses with error details. The responses include an HTTP status code and a descriptive error message.
 * 
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger for logging exception details.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        LOGGER.error("Resource not found: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 400 (Bad Request).
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception) {
        LOGGER.error("Bad request: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AuthenticationException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 401 (Unauthorized).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exception) {
        LOGGER.error("Authentication failed: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles DuplicateResourceException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResourceException(DuplicateResourceException exception) {
        LOGGER.error("Duplicate resource: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles InvalidInputException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 400 (Bad Request).
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiError> handlerInvalidInputException(InvalidInputException exception) {
        LOGGER.error("Invalid input: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DataIntegrityException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 422 (Unprocessable Entity).
     */
    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<ApiError> handlerDataIntegrityException(DataIntegrityException exception) {
        LOGGER.error("Data integrity error: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handles DataIntegrityViolationException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        LOGGER.error("Data integrity violation: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), "Database constraint violation occurred");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles OptimisticLockingFailureException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLocking(OptimisticLockingFailureException exception) {
        LOGGER.error("Optimistic locking failure: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), "The record was updated by another user while you were editing it");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles QueryTimeoutException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 408 (Request Timeout).
     */
    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiError> handleQueryTimeout(QueryTimeoutException exception) {
        LOGGER.error("Query timeout: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.REQUEST_TIMEOUT.value(), "The database query timed out");
        return new ResponseEntity<>(apiError, HttpStatus.REQUEST_TIMEOUT);
    }

    /**
     * Handles JpaSystemException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ApiError> handleJpaSystemException(JpaSystemException exception) {
        LOGGER.error("JPA system exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A database error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles PersistenceException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ApiError> handlePersistenceException(PersistenceException exception) {
        LOGGER.error("Persistence exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A database persistence error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles TransactionSystemException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleTransactionSystemException(TransactionSystemException exception) {
        LOGGER.error("Transaction system exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A database transaction error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles CannotAcquireLockException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(CannotAcquireLockException.class)
    public ResponseEntity<ApiError> handleCannotAcquireLock(CannotAcquireLockException exception) {
        LOGGER.error("Cannot acquire lock: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), "Database resource is currently locked");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles DeadlockLoserDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(DeadlockLoserDataAccessException.class)
    public ResponseEntity<ApiError> handleDeadlockLoser(DeadlockLoserDataAccessException exception) {
        LOGGER.error("Deadlock detected: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), "A database deadlock was detected");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles InvalidDataAccessResourceUsageException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 400 (Bad Request).
     */
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ApiError> handleInvalidDataAccessResourceUsage(InvalidDataAccessResourceUsageException exception) {
        LOGGER.error("Invalid data access resource usage: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Invalid database query or operation");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException exception) {
        LOGGER.error("Data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A general data access error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles NonTransientDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(NonTransientDataAccessException.class)
    public ResponseEntity<ApiError> handleNonTransientDataAccessException(NonTransientDataAccessException exception) {
        LOGGER.error("Non-transient data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A non-recoverable database error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles TransientDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 503 (Service Unavailable).
     */
    @ExceptionHandler(TransientDataAccessException.class)
    public ResponseEntity<ApiError> handleTransientDataAccessException(TransientDataAccessException exception) {
        LOGGER.error("Transient data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(), "A temporary database error occurred, please try again");
        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles RecoverableDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 503 (Service Unavailable).
     */
    @ExceptionHandler(RecoverableDataAccessException.class)
    public ResponseEntity<ApiError> handleRecoverableDataAccessException(RecoverableDataAccessException exception) {
        LOGGER.error("Recoverable data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.SERVICE_UNAVAILABLE.value(), "A recoverable database error occurred, please try again");
        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Handles DuplicateKeyException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 409 (Conflict).
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiError> handleDuplicateKeyException(DuplicateKeyException exception) {
        LOGGER.error("Duplicate key exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.value(), "A record with this identifier already exists");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    /**
     * Handles EmptyResultDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 404 (Not Found).
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
        LOGGER.error("Empty result data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), "The requested record was not found");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IncorrectResultSizeDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ApiError> handleIncorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException exception) {
        LOGGER.error("Incorrect result size data access exception: {}", exception.getMessage(), exception);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The query returned an unexpected number of results");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles PermissionDeniedDataAccessException.
     * 
     * @param exception the exception to handle.
     * @return a ResponseEntity containing the API error details and the HTTP status code 403 (Forbidden).
     */
    @ExceptionHandler(PermissionDeniedDataAccessException.class)
    public ResponseEntity<ApiError> handlePermissionDeniedDataAccessException(PermissionDeniedDataAccessException ex) {
        LOGGER.error("Permission denied data access exception: {}", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN.value(), "Insufficient database permissions for this operation");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles any unexpected Exception.
     * 
     * @param ex the unexpected exception.
     * @return a ResponseEntity containing the API error details and the HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        LOGGER.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                "An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
