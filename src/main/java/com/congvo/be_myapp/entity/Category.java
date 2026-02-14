package com.congvo.be_myapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="categories")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String color;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

}
