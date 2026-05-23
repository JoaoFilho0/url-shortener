package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;

    ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public String generateCode() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public ShortUrl createShortenerUrl(ShortUrl url) {
        return this.shortUrlRepository.save(url);
    }

    public ShortUrl getShortUrlByShortCode(String code) {
        return this.shortUrlRepository.findByShortCode(code);
    }
}
