package com.joaofilho.url_shortener.dto;

import com.joaofilho.url_shortener.Model.ShortUrl;

public record ShortUrlShortenResponse(
        String urlCode
) {
    public ShortUrlShortenResponse(ShortUrl url) {
        this(url.getShortCode());
    }
}
