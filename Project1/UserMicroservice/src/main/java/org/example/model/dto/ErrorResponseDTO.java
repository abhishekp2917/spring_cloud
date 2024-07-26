package org.example.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    @JsonIgnore
    private StackTraceElement[] stackTrace;
}
