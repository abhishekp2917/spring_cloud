package org.example.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.APIResponseDTO;
import org.example.dto.ErrorResponseDTO;
import org.example.dto.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for building and sending API responses.
 * This class provides standardized methods to construct error and success responses,
 * both for use in Spring's ResponseEntity and directly writing to HttpServletResponse.
 * This approach centralizes response formatting and error handling, ensuring consistency across the application.
 */
public class ResponseUtil {

    /**
     * Builds an error response entity to be returned to the client.
     * The error response contains detailed information such as timestamp, status code, error message,
     * and optionally the stack trace of the exception.
     *
     * @param status HTTP status code for the response.
     * @param ex Exception that caused the error (can be null if not applicable).
     * @param error Description of the error.
     * @return A ResponseEntity containing the error details.
     */
    public static ResponseEntity<APIResponseDTO> buildErrorResponse(HttpStatus status, Exception ex, String error) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .error(error)
                .build();
        if(ex!=null) {
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStackTrace(ex.getStackTrace());
        }
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Builds a success response entity to be returned to the client.
     * The success response contains a timestamp, status code, and the response body.
     *
     * @param status HTTP status code for the response.
     * @param body The response body, typically the result of a successful operation.
     * @return A ResponseEntity containing the success details.
     */
    public static ResponseEntity<APIResponseDTO> buildSuccessResponse(HttpStatus status, Object body) {
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .body(body)
                .build();
        return new ResponseEntity<>(successResponse, status);
    }

    /**
     * Directly writes an error response to the HttpServletResponse.
     * Useful for scenarios where a ResponseEntity is not suitable, such as in filters or interceptors.
     *
     * @param status HTTP status code for the response.
     * @param ex Exception that caused the error (can be null if not applicable).
     * @param error Description of the error.
     * @param response The HttpServletResponse object to which the error details will be written.
     * @throws IOException If an input or output exception occurs during the writing process.
     */
    public static void setErrorResponse(HttpStatus status, Exception ex, String error, HttpServletResponse response) throws IOException {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .error(error)
                .build();
        if(ex!=null) {
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStackTrace(ex.getStackTrace());
        }
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
        catch (IOException e) {
            response.getWriter().close();
            throw e;
        }
    }

    /**
     * Directly writes a success response to the HttpServletResponse.
     * This method is typically used in scenarios where a controller method does not return a ResponseEntity,
     * but a response still needs to be sent.
     *
     * @param status HTTP status code for the response.
     * @param body The response body, typically the result of a successful operation.
     * @param response The HttpServletResponse object to which the success details will be written.
     * @throws IOException If an input or output exception occurs during the writing process.
     */
    public static void setSuccessResponse(HttpStatus status, Object body, HttpServletResponse response) throws IOException {
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .body(body)
                .build();
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));
        }
        catch (IOException e) {
            response.getWriter().close();
            throw e;
        }
    }
}
