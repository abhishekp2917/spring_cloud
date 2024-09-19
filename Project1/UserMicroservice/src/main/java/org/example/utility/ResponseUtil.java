package org.example.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ErrorResponseDTO;
import org.example.dto.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for building consistent API responses.
 * <p>
 * This class provides methods for constructing and returning error and success responses
 * in a standard format for both REST APIs (through {@link ResponseEntity}) and servlet responses (through {@link HttpServletResponse}).
 * It supports serialization of response bodies into JSON format using the Jackson library.
 * </p>
 */
public class ResponseUtil {

    /**
     * Builds a structured error response for REST APIs.
     * <p>
     * This method creates an {@link ErrorResponseDTO} object with details about the error,
     * such as a timestamp, status code, and error message. It also optionally includes
     * the exception's stack trace and message if provided.
     * </p>
     *
     * @param status the HTTP status to set for the response.
     * @param ex the exception that caused the error (can be null).
     * @param error a brief description of the error.
     * @return a {@link ResponseEntity} containing the error response and HTTP status.
     */
    public static ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, Exception ex, String error) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .error(error)
                .build();
        if (ex != null) {
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStackTrace(ex.getStackTrace());
        }
        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * Builds a structured success response for REST APIs.
     * <p>
     * This method creates a {@link SuccessResponseDTO} object, encapsulating a success message,
     * the response body (optional), and a timestamp. It returns this as part of a {@link ResponseEntity}.
     * </p>
     *
     * @param status the HTTP status to set for the response.
     * @param body the body of the response (can be null).
     * @param message a brief success message.
     * @return a {@link ResponseEntity} containing the success response and HTTP status.
     */
    public static ResponseEntity<SuccessResponseDTO> buildSuccessResponse(HttpStatus status, Object body, String message) {
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .message(message)
                .body(body)
                .build();
        return new ResponseEntity<>(successResponse, status);
    }

    /**
     * Writes an error response directly to the {@link HttpServletResponse}.
     * <p>
     * This method constructs an {@link ErrorResponseDTO}, serializes it into JSON,
     * and writes it to the response output stream. The status and content type are set accordingly.
     * </p>
     *
     * @param status the HTTP status to set for the response.
     * @param ex the exception that caused the error (can be null).
     * @param error a brief description of the error.
     * @param response the {@link HttpServletResponse} to write the response to.
     * @throws IOException if an input or output exception occurs while writing the response.
     */
    public static void setErrorResponse(HttpStatus status, Exception ex, String error, HttpServletResponse response) throws IOException {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .error(error)
                .build();
        if (ex != null) {
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStackTrace(ex.getStackTrace());
        }
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } catch (IOException e) {
            response.getWriter().close();
            throw e;
        }
    }

    /**
     * Writes a success response directly to the {@link HttpServletResponse}.
     * <p>
     * This method constructs a {@link SuccessResponseDTO}, serializes it into JSON,
     * and writes it to the response output stream. The status and content type are set accordingly.
     * </p>
     *
     * @param status the HTTP status to set for the response.
     * @param body the body of the response (can be null).
     * @param message a brief success message.
     * @param response the {@link HttpServletResponse} to write the response to.
     * @throws IOException if an input or output exception occurs while writing the response.
     */
    public static void setSuccessResponse(HttpStatus status, Object body, String message, HttpServletResponse response) throws IOException {
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .message(message)
                .body(body)
                .build();
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));
        } catch (IOException e) {
            response.getWriter().close();
            throw e;
        }
    }
}
