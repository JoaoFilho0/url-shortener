package com.joaofilho.url_shortener;

import com.joaofilho.url_shortener.repository.ShortUrlRepository;
import com.joaofilho.url_shortener.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration," +
				"org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration," +
				"org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration",
		"api.security.token.secret=test-secret"
})
class UrlShortenerApplicationTests {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private ShortUrlRepository shortUrlRepository;

	@Test
	void contextLoads() {
	}

}
