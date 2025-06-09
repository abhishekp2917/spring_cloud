package org.example.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Extract the HTTP status code from the response
        HttpStatus status = HttpStatus.resolve(response.status());

        // Handle different status codes
        switch (status) {
            case NOT_FOUND:
                // Handle 404 Not Found
                return new BadRequestException("Resource not found for method: " + methodKey);
            case INTERNAL_SERVER_ERROR:
                // Handle 500 Internal Server Error
                return new InternalServerErrorException("Internal server error occurred for method: " + methodKey);
            default:
                // Handle other status codes or default behavior
                return new Exception("Generic error occurred for method: " + methodKey);
        }
    }
}

