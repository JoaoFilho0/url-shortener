package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @Test
    void shouldGenerateCodeWithTenAlphanumericCharacters() {
        String code = shortUrlService.generateCode();

        assertThat(code)
                .hasSize(10)
                .matches("[A-Za-z0-9]+");
    }

    @Test
    void shouldCreateShortenerUrl() {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://example.com");
        shortUrl.setShortCode("abc123XYZ0");

        when(shortUrlRepository.save(shortUrl)).thenReturn(shortUrl);

        ShortUrl result = shortUrlService.createShortenerUrl(shortUrl);

        assertThat(result).isSameAs(shortUrl);
        verify(shortUrlRepository).save(shortUrl);
    }

    @Test
    void shouldGetShortUrlByShortCode() {
        String code = "abc123XYZ0";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://example.com");
        shortUrl.setShortCode(code);

        when(shortUrlRepository.findByShortCode(code)).thenReturn(shortUrl);

        ShortUrl result = shortUrlService.getShortUrlByShortCode(code);

        assertThat(result).isSameAs(shortUrl);
        verify(shortUrlRepository).findByShortCode(code);
    }

    @Test
    void shouldGetAllShortUrls() {
        ShortUrl firstShortUrl = new ShortUrl();
        firstShortUrl.setOriginalUrl("https://example.com");
        firstShortUrl.setShortCode("abc123XYZ0");

        ShortUrl secondShortUrl = new ShortUrl();
        secondShortUrl.setOriginalUrl("https://openai.com");
        secondShortUrl.setShortCode("xyz987ABC1");

        List<ShortUrl> shortUrls = List.of(firstShortUrl, secondShortUrl);
        when(shortUrlRepository.findAll()).thenReturn(shortUrls);

        List<ShortUrl> result = shortUrlService.getAllShortUrls();

        assertThat(result).containsExactly(firstShortUrl, secondShortUrl);
        verify(shortUrlRepository).findAll();
    }
}
