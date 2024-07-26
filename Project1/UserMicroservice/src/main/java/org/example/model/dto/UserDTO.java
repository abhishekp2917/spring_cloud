package org.example.model.dto;

import lombok.*;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<AuthorityDTO> authorities;
}
