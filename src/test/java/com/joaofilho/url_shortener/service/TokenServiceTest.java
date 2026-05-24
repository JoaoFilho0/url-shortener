package com.joaofilho.url_shortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    private static final String SECRET = "test-secret";

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);
    }

    @Test
    void shouldGenerateTokenForUser() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("john@example.com");

        String token = tokenService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(tokenService.validateToken(token)).isEqualTo("john@example.com");
    }

    @Test
    void shouldReturnSubjectWhenTokenIsValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("jane@example.com");
        String token = tokenService.generateToken(userDetails);

        String subject = tokenService.validateToken(token);

        assertThat(subject).isEqualTo("jane@example.com");
    }

    @Test
    void shouldReturnEmptyStringWhenTokenIsInvalid() {
        String subject = tokenService.validateToken("invalid-token");

        assertThat(subject).isEmpty();
    }
}
