package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.dto.ShortUrlShortenRequestDTO;
import com.joaofilho.url_shortener.exception.ResourceNotFoundException;
import com.joaofilho.url_shortener.repository.ShortUrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

import static com.joaofilho.url_shortener.exception.ExceptionMessages.COULD_NOT_GENERATE_A_UNIQUE_SHORT_CODE;
import static com.joaofilho.url_shortener.exception.ExceptionMessages.SHORT_URL_NOT_FOUND;

@Service
public class ShortUrlService {
    private static final int MAX_ATTEMPTS = 5;
    private static final int CODE_LENGTH = 10;
    private static final String CODE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final ShortUrlRepository shortUrlRepository;

    ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = SECURE_RANDOM.nextInt(CODE_ALPHABET.length());
            code.append(CODE_ALPHABET.charAt(index));
        }

        return code.toString();
    }

    @Transactional
    public ShortUrl createShortenerUrl(ShortUrlShortenRequestDTO urlDTO) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String code = generateCode();

            if (this.shortUrlRepository.existsByShortCode(code)) {
                continue;
            }

            ShortUrl shortUrl = new ShortUrl();
            shortUrl.setOriginalUrl(urlDTO.url());
            shortUrl.setShortCode(code);

            try {
                return shortUrlRepository.save(shortUrl);
            } catch (DataIntegrityViolationException _) {
            }
        }

        throw new IllegalStateException(COULD_NOT_GENERATE_A_UNIQUE_SHORT_CODE);
    }

    public ShortUrl getShortUrlByShortCode(String code) {
        return this.shortUrlRepository.findByShortCode(code).orElseThrow(() -> new ResourceNotFoundException(SHORT_URL_NOT_FOUND));
    }

    public List<ShortUrl> getAllShortUrls() {
        return this.shortUrlRepository.findAll();
    }
}
