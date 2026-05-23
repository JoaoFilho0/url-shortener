package com.joaofilho.url_shortener.controller;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.dto.ShortUrlShortenRequest;
import com.joaofilho.url_shortener.dto.ShortUrlShortenResponse;
import com.joaofilho.url_shortener.service.ShortUrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/url")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping
    public ResponseEntity<ShortUrlShortenResponse> shorten(@RequestBody ShortUrlShortenRequest url) {
        String code = this.shortUrlService.generateCode();

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(url.url());
        shortUrl.setShortCode(code);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(shortUrl.getShortCode())
                .toUri();

        this.shortUrlService.createShortenerUrl(shortUrl);

        return ResponseEntity.created(uri).body(new ShortUrlShortenResponse(shortUrl));
    }

    @GetMapping("/{code}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String code) {
        return ResponseEntity.ok(this.shortUrlService.getShortUrlByShortCode(code).getOriginalUrl());
    }
}
