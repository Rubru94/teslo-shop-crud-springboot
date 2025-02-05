package com.teslo.teslo_shop;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TesloShopApplicationTests {

	@Mock
	private SpringApplication springApplication;

	@Test
	void contextLoads() {
		// This test ensures that application context is loaded correctly.
	}

	@Test
	public void testMain() {
		try (var mock = mockStatic(SpringApplication.class)) {
			TesloShopApplication.main(new String[] {});
			mock.verify(() -> SpringApplication.run(TesloShopApplication.class, new String[] {}));
		}
	}

}
