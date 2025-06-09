package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseDTO extends APIResponseDTO {

    private String error;
    private String message;
    @JsonIgnore
    private StackTraceElement[] stackTrace;
}
