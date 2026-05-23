package com.joaofilho.url_shortener.repository;

import com.joaofilho.url_shortener.Model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    ShortUrl findByShortCode(String code);
}
