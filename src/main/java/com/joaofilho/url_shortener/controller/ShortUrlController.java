package com.joaofilho.url_shortener.controller;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.dto.ShortUrlShortenRequestDTO;
import com.joaofilho.url_shortener.dto.ShortUrlShortenResponseDTO;
import com.joaofilho.url_shortener.service.ShortUrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shortener_url")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping
    public ResponseEntity<ShortUrlShortenResponseDTO> shorten(@RequestBody @Valid ShortUrlShortenRequestDTO url) {
        ShortUrl shortUrl = this.shortUrlService.createShortenerUrl(url);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(shortUrl.getShortCode())
                .toUri();

        return ResponseEntity.created(uri).body(new ShortUrlShortenResponseDTO(shortUrl));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String code) {
        ShortUrl shortUrl = shortUrlService.getShortUrlByShortCode(code);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(shortUrl.getOriginalUrl()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ShortUrlShortenResponseDTO>> getAllShortUrls() {
        return ResponseEntity
                .ok(
                        this.shortUrlService.getAllShortUrls()
                                .stream()
                                .map(ShortUrlShortenResponseDTO::new)
                                .toList()
                );
    }
}
