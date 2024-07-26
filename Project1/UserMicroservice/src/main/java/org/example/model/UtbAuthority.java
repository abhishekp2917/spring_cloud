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
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UtbUser> users;
    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UtbPermission> permissions;
}
