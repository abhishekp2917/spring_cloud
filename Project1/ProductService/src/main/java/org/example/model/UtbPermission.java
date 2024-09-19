package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UtbPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissionID")
    private long id;
    private String service;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "UtbAuthorityPermission",
            joinColumns = @JoinColumn(name = "permissionID"),
            inverseJoinColumns = @JoinColumn(name = "authorityID")
    )
    private Set<UtbAuthority> authorities;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UtbRolePermission",
            joinColumns = @JoinColumn(name = "permissionID"),
            inverseJoinColumns = @JoinColumn(name = "roleID")
    )
    private Set<UtbRole> roles;
}
