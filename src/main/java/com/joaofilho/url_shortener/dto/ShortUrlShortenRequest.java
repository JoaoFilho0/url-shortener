package com.joaofilho.url_shortener.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortUrlShortenRequest(
        @URL
        @NotEmpty
        String url
) {
}
