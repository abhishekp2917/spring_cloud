package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class APIResponseDTO <T> {

    private String timestamp;
    private int status;
    private T body;
    private String error;
    private String message;
}
