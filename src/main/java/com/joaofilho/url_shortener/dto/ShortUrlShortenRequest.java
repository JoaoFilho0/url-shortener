package com.joaofilho.url_shortener.dto;

import jakarta.validation.constraints.NotEmpty;

public record ShortUrlShortenRequest(
        @NotEmpty
        String url
) {
}
