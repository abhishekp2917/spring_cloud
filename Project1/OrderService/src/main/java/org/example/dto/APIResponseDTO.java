package org.example.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseDTO <T> {

    private String timestamp;
    private int status;
    private T body;
    private String error;
    private String message;
}
