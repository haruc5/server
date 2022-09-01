package com.example.onedaypiece;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OnedayPieceApplicationTests {
	@Value("${spring.resource.path}")
	private String path;

	@Test
	void contextLoads() {
		System.out.println("path: " +  path);
	}

}
