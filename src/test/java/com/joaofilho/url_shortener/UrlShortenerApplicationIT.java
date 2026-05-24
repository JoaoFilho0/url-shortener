package com.joaofilho.url_shortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "api.security.token.secret=test-secret")
class UrlShortenerApplicationIT {

	@Test
	void contextLoadsWithDatabaseAndMigrations() {
	}
}
