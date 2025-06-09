package org.example.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * The @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id") annotation prevents
 * infinite recursion during JSON serialization by using the id property as a unique identifier for the object.
 * This ensures that when the object is serialized, only the identifier is used for repeated references, avoiding cyclic references in bidirectional relationships.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UtbProduct {

    @Id
    @Column(name = "productID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne()
    @JoinColumn(name = "categoryID", nullable = false)
    private UtbCategory category;
    private BigDecimal price;
    private String description;
}
