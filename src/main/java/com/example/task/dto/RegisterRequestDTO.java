package com.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

