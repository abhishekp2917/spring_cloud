package org.example.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SuccessResponseDTO extends APIResponseDTO {

    private Object body;
}