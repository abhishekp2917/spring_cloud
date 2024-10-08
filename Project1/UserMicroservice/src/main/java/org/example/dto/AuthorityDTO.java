package org.example.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDTO {
    private Long id;
    private String name;
}