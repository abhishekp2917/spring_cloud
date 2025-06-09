package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private String description;
}
