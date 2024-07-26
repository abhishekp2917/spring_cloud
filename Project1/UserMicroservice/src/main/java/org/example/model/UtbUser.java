package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UtbUser {

    @Id
    @Column(name = "userID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;
    public String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UtbUserAuthority",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "authorityID")
    )
    private Set<UtbAuthority> authorities;
}
