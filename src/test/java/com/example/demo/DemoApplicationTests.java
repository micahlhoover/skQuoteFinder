package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertTrue;

//import org.junit.Assert;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void quoteIsRetrieved() {
		var controller = new KierkegaardController();
		assertTrue(controller.LoadQuotes());
	}

	@Test
	void specificIsRetrieved() {
		var controller = new KierkegaardController();
		controller.LoadQuotes();

		boolean foundQuote = false;

		for (Quote quote : controller._quotes) {
			if (quote.Text.contains("mortal"))
			foundQuote = true;
		}

		assertTrue(foundQuote);
	}

}
