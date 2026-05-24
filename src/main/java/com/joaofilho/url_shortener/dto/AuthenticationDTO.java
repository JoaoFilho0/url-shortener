package com.joaofilho.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record AuthenticationDTO(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
