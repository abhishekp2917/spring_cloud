package org.example.utility;

import org.example.model.UtbRole;
import org.example.model.UtbUser;
import org.example.dto.AuthorityDTO;
import org.example.dto.UserDTO;

import java.util.stream.Collectors;

/**
 * Utility class for mapping domain models to their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This class provides methods for converting entities such as {@link UtbUser} and {@link UtbRole}
 * to their corresponding DTOs, {@link UserDTO} and {@link AuthorityDTO}, which are commonly used
 * for transferring data between layers in a clean and optimized manner.
 * </p>
 * <p>
 * These methods ensure that domain entities are transformed into simpler representations
 * to be consumed by clients (such as API responses), while maintaining separation of concerns
 * between the domain and the presentation layer.
 * </p>
 */
public class DTOMapperUtil {

    /**
     * Converts a {@link UtbUser} entity to a {@link UserDTO}.
     * <p>
     * This method maps the essential properties of a user entity, including the user's ID, username, email,
     * and their associated roles. The roles are further converted into a collection of {@link AuthorityDTO}
     * objects using the {@link #toRoleDTO(UtbRole)} method.
     * </p>
     *
     * @param user the {@link UtbUser} entity to be mapped to a DTO.
     * @return a {@link UserDTO} representing the mapped user data.
     */
    public static UserDTO toUserDTO(UtbUser user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(user.getRoles().stream()
                        .map(DTOMapperUtil::toRoleDTO)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Converts a {@link UtbRole} entity to an {@link AuthorityDTO}.
     * <p>
     * This method maps the essential properties of a role entity, including the role's ID and name,
     * to a simpler representation used for transferring role information, typically as part of a user object.
     * </p>
     *
     * @param role the {@link UtbRole} entity to be mapped to a DTO.
     * @return an {@link AuthorityDTO} representing the mapped role data.
     */
    public static AuthorityDTO toRoleDTO(UtbRole role) {
        return AuthorityDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
