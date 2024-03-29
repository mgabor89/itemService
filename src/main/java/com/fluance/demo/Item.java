package com.fluance.demo;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private final String name;
    private String description;

    public Item() {
        name = "";
        description = "";
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = Optional.ofNullable(description).orElse("");
    }

   Item(Long id, String name, String description) {
        this(name, description);
        this.id = id;
    }
}
