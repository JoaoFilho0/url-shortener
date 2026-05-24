package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.User;
import com.joaofilho.url_shortener.Model.UserRole;
import com.joaofilho.url_shortener.dto.RegisterDTO;
import com.joaofilho.url_shortener.exception.AlreadyExistException;
import com.joaofilho.url_shortener.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserWithEncryptedPasswordAndUserRole() {
        RegisterDTO request = new RegisterDTO("john@example.com", "plain-password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encrypted-password");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register(request);

        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getPassword()).isEqualTo("encrypted-password");
        assertThat(result.getUserRole()).isEqualTo(UserRole.USER);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isSameAs(result);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterDTO request = new RegisterDTO("john@example.com", "plain-password");
        User existingUser = new User("john@example.com", "encrypted-password", UserRole.USER);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.<UserDetails>of(existingUser));

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage("Email address already exist");
        verify(passwordEncoder, never()).encode(request.password());
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any(User.class));
    }
}
