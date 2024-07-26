package org.example.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessResponseDTO {

    private String timestamp;
    private int status;
    private String message;
    private Object body;
}