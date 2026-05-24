package com.joaofilho.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ShortUrlShortenRequestDTO(
        @URL
        @NotBlank
        String url
) {
}
