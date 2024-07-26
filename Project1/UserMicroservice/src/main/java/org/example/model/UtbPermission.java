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
public class UtbPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissionID")
    private long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UtbAuthorityPermission",
            joinColumns = @JoinColumn(name = "permissionID"),
            inverseJoinColumns = @JoinColumn(name = "authorityID")
    )
    private Set<UtbAuthority> authorities;
}
