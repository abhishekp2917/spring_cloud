package org.example.utility;

import org.example.model.UtbAuthority;
import org.example.model.UtbUser;
import org.example.model.dto.AuthorityDTO;
import org.example.model.dto.UserDTO;
import java.util.stream.Collectors;

public class DTOMapperUtil {

    public static UserDTO toUserDTO(UtbUser user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getAuthorities().stream()
                        .map(DTOMapperUtil::toAuthorityDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static AuthorityDTO toAuthorityDTO(UtbAuthority authority) {
        return AuthorityDTO.builder()
                .id(authority.getId())
                .name(authority.getName())
                .build();
    }
}
