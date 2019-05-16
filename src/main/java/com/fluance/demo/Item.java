package com.fluance.demo;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
