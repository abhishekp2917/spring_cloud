package org.example.exception;

import org.example.dto.APIResponseDTO;
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
    public ResponseEntity<APIResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        // Builds an error response indicating that the resource was not found
        return ResponseUtil.buildErrorResponse(HttpStatus.NOT_FOUND, ex, "Resource not found");
    }

    /**
     * Handles ResourceNotFoundException, which is thrown when a request is made to a URL and resource is not available.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 404 Not Found status.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        // Builds an error response indicating that the resource was not found
        return ResponseUtil.buildErrorResponse(HttpStatus.NOT_FOUND, ex, "Resource not found");
    }


    /**
     * Handles ServiceUnavailableException, which is thrown when a service is temporarily unavailable.
     * This can occur if the service is down or overloaded.
     *
     * @param ex The exception instance that was thrown.
     * @return A ResponseEntity containing an error response with HTTP 503 Service Unavailable status.
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<APIResponseDTO> handleServiceUnavailableException(ServiceUnavailableException ex) {
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
    public ResponseEntity<APIResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
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
    public ResponseEntity<APIResponseDTO> handleException(Exception ex) {
        // Builds a generic error response for any unexpected exceptions
        return ResponseUtil.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, "Internal Server Error");
    }
}
