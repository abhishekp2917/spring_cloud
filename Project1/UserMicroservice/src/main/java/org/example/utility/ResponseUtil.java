package org.example.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.dto.ErrorResponseDTO;
import org.example.model.dto.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseUtil {

    public static ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, Exception ex, String error) {
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

    public static ResponseEntity<SuccessResponseDTO> buildSuccessResponse(HttpStatus status, Object body, String message) {
        SuccessResponseDTO successResponse = SuccessResponseDTO.builder()
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .status(status.value())
                .message(message)
                .body(body)
                .build();
        return new ResponseEntity<>(successResponse, status);
    }

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
        }
        catch (IOException e) {
            response.getWriter().close();
            throw e;
        }
    }
}
