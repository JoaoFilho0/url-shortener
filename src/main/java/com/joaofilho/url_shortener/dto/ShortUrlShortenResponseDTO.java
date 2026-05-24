package com.joaofilho.url_shortener.dto;

import com.joaofilho.url_shortener.Model.ShortUrl;

public record ShortUrlShortenResponseDTO(
        String originalUrl,
        String urlCode
) {
    public ShortUrlShortenResponseDTO(ShortUrl url) {
        this(url.getOriginalUrl(), url.getShortCode());
    }
}
