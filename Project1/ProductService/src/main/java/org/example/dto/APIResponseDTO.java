package org.example.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class APIResponseDTO {

    private String timestamp;
    private int status;
}
