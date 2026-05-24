package com.joaofilho.url_shortener.dto;

import com.joaofilho.url_shortener.Model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotNull
        UserRole role
) {
}
