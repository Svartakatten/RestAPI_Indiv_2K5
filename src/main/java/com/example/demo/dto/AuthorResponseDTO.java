package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthorResponseDTO {
    @NotNull
    private Long id;
    @NotBlank(message = "Author name is required")
    private String name;

    // Gets
    public Long getId() { return id; }

    public String getName() { return name; }

    // Sets
    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

}
