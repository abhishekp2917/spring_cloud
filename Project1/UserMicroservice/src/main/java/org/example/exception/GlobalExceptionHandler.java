package org.example.exception;

import org.example.model.dto.ErrorResponseDTO;
import org.example.utility.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.naming.ServiceUnavailableException;

/**
 * @ControllerAdvice is a Spring annotation used to handle exceptions globally across the whole application, providing
 * centralized and consistent error handling for all controllers. It can also be used to apply global configurations
 * such as data binding, formatting, and validation to multiple controllers.
 *
 * @ExceptionHandle annotation is used to define a method that handles exceptions thrown by any controller in a
 * Spring application, allowing for centralized and consistent exception handling.
 *
 * In Spring, exception handlers are resolved based on the specificity of the exception type they handle. If you want
 * to handle a specific exception explicitly, you should place that handler method before the general RuntimeException
 * or Exception handler. This ensures that the specific handler is invoked first when that particular exception is thrown.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NoHandlerFoundException, which is thrown when a request is made to a URL that does not exist.
     * This typically occurs when the requested resource or endpoint is not found on the server.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 404 Not Found status.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(NoHandlerFoundException ex) {
        // Builds an error response indicating that the resource was not found
        return ResponseUtil.buildErrorResponse(HttpStatus.NOT_FOUND, ex, "Resource not found");
    }

    /**
     * Handles InvalidUsernameOrPasswordException, which is thrown when authentication fails due to incorrect username or password.
     * This provides a specific response for authentication failures.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 401 Unauthorized status.
     */
    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidUsernameOrPasswordException(InvalidUsernameOrPasswordException ex) {
        // Builds an error response indicating invalid username or password
        return ResponseUtil.buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, "Invalid username or password");
    }

    /**
     * Handles BadRequestException, which is thrown when a request is invalid or cannot be processed due to bad input.
     * This is used to manage situations where the client sends invalid data.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 400 Bad Request status.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        // Builds an error response indicating a bad request from the client
        return ResponseUtil.buildErrorResponse(HttpStatus.BAD_REQUEST, ex, "Bad Request");
    }

    /**
     * Handles ServiceUnavailableException, which is thrown when a service is temporarily unavailable.
     * This can occur if the service is down or overloaded.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 503 Service Unavailable status.
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleServiceUnavailableException(ServiceUnavailableException ex) {
        // Builds an error response indicating that the service is currently unavailable
        return ResponseUtil.buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, ex, "Service Unavailable");
    }

    /**
     * Handles HttpRequestMethodNotSupportedException, which is thrown when an HTTP request method (e.g., POST, GET) is not supported by the endpoint.
     * This ensures that methods not allowed on a particular endpoint are handled appropriately.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 405 Method Not Allowed status.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        // Builds an error response indicating that the HTTP method used is not allowed
        return ResponseUtil.buildErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex, "Invalid Request");
    }

    /**
     * Handles any other exceptions that are not specifically handled by the above methods.
     * This method acts as a fallback to catch all unhandled exceptions, ensuring that any exception results in a meaningful error response.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 500 Internal Server Error status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex) {
        // Builds a generic error response for any unexpected exceptions
        return ResponseUtil.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, "Internal Server Error");
    }
}
