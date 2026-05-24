package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void shouldLoadUserByUsername() {
        String email = "john@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userDetails));

        UserDetails result = authorizationService.loadUserByUsername(email);

        assertThat(result).isSameAs(userDetails);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorizationService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Email address not found");
        verify(userRepository).findByEmail(email);
    }
}
