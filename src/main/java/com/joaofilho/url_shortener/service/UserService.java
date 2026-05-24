package com.joaofilho.url_shortener.service;

import com.joaofilho.url_shortener.Model.User;
import com.joaofilho.url_shortener.Model.UserRole;
import com.joaofilho.url_shortener.dto.RegisterDTO;
import com.joaofilho.url_shortener.exception.AlreadyExistException;
import com.joaofilho.url_shortener.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.joaofilho.url_shortener.exception.ExceptionMessages.EMAIL_ADDRESS_ALREADY_EXIST;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO userDTO) {
        if (this.userRepository.findByEmail(userDTO.email()).isPresent()) throw new AlreadyExistException(EMAIL_ADDRESS_ALREADY_EXIST);

        String encryptPassword = this.passwordEncoder.encode(userDTO.password());
        User newUser = new User(userDTO.email(), encryptPassword, UserRole.USER);

        return this.userRepository.save(newUser);
    }
}
