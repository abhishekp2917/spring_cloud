package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent HTTP 500 Internal Server Error.
 * This exception is used to signal that an unexpected error has occurred on the server side.
 *
 * The @ResponseStatus annotation maps this exception to an HTTP status code of 500 Internal Server Error.
 * This helps in communicating to the client that there was an issue with processing the request due to a server-side error.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    /**
     * Default constructor for InternalServerErrorException.
     * Calls the superclass (RuntimeException) constructor without any arguments.
     * This provides a basic instantiation of the exception.
     */
    public InternalServerErrorException() {
        super();
    }

    /**
     * Constructor for InternalServerErrorException with a custom message.
     * This allows the exception to carry a specific message that describes the server error.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     */
    public InternalServerErrorException(String message) {
        super(message);
    }

    /**
     * Constructor for InternalServerErrorException with a custom message and a cause.
     * This constructor allows the exception to be initialized with both a message and another throwable cause.
     * This is useful for exception chaining, where one exception is caused by another, allowing the propagation of the original cause.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     * @param cause   The cause of the exception (a throwable that caused this exception to be thrown). This allows tracking of the underlying issue.
     */
    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
