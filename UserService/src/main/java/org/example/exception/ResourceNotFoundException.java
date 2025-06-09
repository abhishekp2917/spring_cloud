package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent HTTP 404 Not Found errors.
 * This exception is used when a requested resource cannot be found on the server.
 *
 * The @ResponseStatus annotation maps this exception to an HTTP status code of 404 Not Found.
 * This informs the client that the requested resource does not exist or could not be located.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Default constructor for ResourceNotFoundException.
     * Calls the superclass (RuntimeException) constructor without any arguments.
     * This provides a basic instantiation of the exception.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructor for ResourceNotFoundException with a custom message.
     * This allows the exception to carry a specific message that describes why the resource was not found.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for ResourceNotFoundException with a custom message and a cause.
     * This constructor allows the exception to be initialized with both a message and another throwable cause.
     * This is useful for exception chaining, where one exception is caused by another, allowing the propagation of the original cause.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     * @param cause   The cause of the exception (a throwable that caused this exception to be thrown). This allows tracking of the underlying issue.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
