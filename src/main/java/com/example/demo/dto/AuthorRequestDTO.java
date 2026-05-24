package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorRequestDTO {
    @NotBlank(message = "Author name is required")
    private String name;

    // Gets
    public String getName() { return name; }

    // Sets
    public void setName(String name) { this.name = name; }
}
