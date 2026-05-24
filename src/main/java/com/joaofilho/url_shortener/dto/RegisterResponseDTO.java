package com.joaofilho.url_shortener.dto;

import com.joaofilho.url_shortener.Model.User;
import com.joaofilho.url_shortener.Model.UserRole;

public record RegisterResponseDTO(
        Long id,
        String email,
        UserRole role
) {
    public RegisterResponseDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserRole());
    }
}
