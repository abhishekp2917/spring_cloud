package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent HTTP 400 Bad Request errors.
 * This exception is used to signal that a client has sent a request that is invalid or cannot be processed by the server.
 *
 * The @ResponseStatus annotation maps this exception to an HTTP status code of 400 Bad Request.
 * This provides a standard way of indicating to clients that their request was malformed or contained invalid data.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Default constructor for BadRequestException.
     * Calls the superclass (RuntimeException) constructor without any arguments.
     */
    public BadRequestException() {
        super();
    }

    /**
     * Constructor for BadRequestException with a custom message.
     * This allows the exception to carry a specific message that describes the error.
     *
     * @param message The detail message that explains the reason for the exception.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor for BadRequestException with a custom message and a cause.
     * This constructor allows the exception to be initialized with both a message and another throwable cause.
     * This is useful for exception chaining, where one exception is caused by another.
     *
     * @param message The detail message that explains the reason for the exception.
     * @param cause   The cause of the exception (a throwable that caused this exception to be thrown).
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
