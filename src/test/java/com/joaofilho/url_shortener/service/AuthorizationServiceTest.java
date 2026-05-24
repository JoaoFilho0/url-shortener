package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
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
        when(userRepository.findByEmail(email)).thenReturn(userDetails);

        UserDetails result = authorizationService.loadUserByUsername(email);

        assertThat(result).isSameAs(userDetails);
        verify(userRepository).findByEmail(email);
    }
}
