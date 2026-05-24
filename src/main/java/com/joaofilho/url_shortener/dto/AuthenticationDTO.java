package com.joaofilho.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
