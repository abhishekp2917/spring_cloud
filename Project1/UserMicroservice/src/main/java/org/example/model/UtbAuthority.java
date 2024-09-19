package org.example.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UtbAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authorityID")
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UtbAuthorityRoles",
            joinColumns = @JoinColumn(name = "authorityID"),
            inverseJoinColumns = @JoinColumn(name = "roleID")
    )
    private Set<UtbRole> roles;
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UtbPermission> permissions;
}
