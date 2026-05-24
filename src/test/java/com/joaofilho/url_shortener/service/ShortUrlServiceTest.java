package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.ShortUrl;
import com.joaofilho.url_shortener.dto.ShortUrlShortenRequestDTO;
import com.joaofilho.url_shortener.repository.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
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
        ShortUrlShortenRequestDTO request = new ShortUrlShortenRequestDTO("https://example.com");

        when(shortUrlRepository.existsByShortCode(any())).thenReturn(false);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = shortUrlService.createShortenerUrl(request);

        assertThat(result.getOriginalUrl()).isEqualTo("https://example.com");
        assertThat(result.getShortCode())
                .hasSize(10)
                .matches("[A-Za-z0-9]+");
        verify(shortUrlRepository).existsByShortCode(result.getShortCode());
        verify(shortUrlRepository).save(result);
    }

    @Test
    void shouldTryAnotherCodeWhenGeneratedCodeAlreadyExists() {
        ShortUrlService service = spy(new ShortUrlService(shortUrlRepository));
        ShortUrlShortenRequestDTO request = new ShortUrlShortenRequestDTO("https://example.com");

        doReturn("existing01", "unique0001").when(service).generateCode();
        when(shortUrlRepository.existsByShortCode("existing01")).thenReturn(true);
        when(shortUrlRepository.existsByShortCode("unique0001")).thenReturn(false);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = service.createShortenerUrl(request);

        assertThat(result.getOriginalUrl()).isEqualTo("https://example.com");
        assertThat(result.getShortCode()).isEqualTo("unique0001");
        verify(shortUrlRepository, never()).save(
                org.mockito.ArgumentMatchers.argThat(shortUrl -> "existing01".equals(shortUrl.getShortCode()))
        );
        verify(shortUrlRepository).save(result);
    }

    @Test
    void shouldTryAnotherCodeWhenDatabaseRejectsDuplicatedCode() {
        ShortUrlService service = spy(new ShortUrlService(shortUrlRepository));
        ShortUrlShortenRequestDTO request = new ShortUrlShortenRequestDTO("https://example.com");

        doReturn("race000001", "race000002").when(service).generateCode();
        when(shortUrlRepository.existsByShortCode(any())).thenReturn(false);
        doThrow(new DataIntegrityViolationException("duplicated code"))
                .doAnswer(invocation -> invocation.getArgument(0))
                .when(shortUrlRepository)
                .save(any(ShortUrl.class));

        ShortUrl result = service.createShortenerUrl(request);

        assertThat(result.getShortCode()).isEqualTo("race000002");
        verify(shortUrlRepository, times(2)).save(any(ShortUrl.class));
    }

    @Test
    void shouldThrowExceptionWhenUniqueCodeCannotBeGenerated() {
        ShortUrlService service = spy(new ShortUrlService(shortUrlRepository));
        ShortUrlShortenRequestDTO request = new ShortUrlShortenRequestDTO("https://example.com");

        doReturn("duplicated").when(service).generateCode();
        when(shortUrlRepository.existsByShortCode("duplicated")).thenReturn(true);

        assertThatThrownBy(() -> service.createShortenerUrl(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Could not generate a unique short code");
        verify(shortUrlRepository, times(5)).existsByShortCode("duplicated");
        verify(shortUrlRepository, never()).save(any(ShortUrl.class));
    }

    @Test
    void shouldGetShortUrlByShortCode() {
        String code = "abc123XYZ0";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl("https://example.com");
        shortUrl.setShortCode(code);

        when(shortUrlRepository.findByShortCode(code)).thenReturn(Optional.of(shortUrl));

        ShortUrl result = shortUrlService.getShortUrlByShortCode(code);

        assertThat(result).isSameAs(shortUrl);
        verify(shortUrlRepository).findByShortCode(code);
    }

    @Test
    void shouldThrowExceptionWhenShortUrlDoesNotExist() {
        String code = "missing001";
        when(shortUrlRepository.findByShortCode(code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shortUrlService.getShortUrlByShortCode(code))
                .isInstanceOf(com.joaofilho.url_shortener.exception.ResourceNotFoundException.class)
                .hasMessage("Short url not found");
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
