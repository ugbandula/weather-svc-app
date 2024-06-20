/**
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */
package com.weather.demo;

import com.weather.demo.api.controller.WeatherController;
import com.weather.demo.api.exception.AccessDeniedException;
import com.weather.demo.api.exception.BadRequestException;
import com.weather.demo.api.model.dto.CityWeatherStatusDto;
import com.weather.demo.api.service.WeatherService;
import com.weather.demo.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private WebApplicationContext applicationContext;

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private WeatherController weatherController;

	@Test
	void contextLoads() {
		assertThat(weatherService).isNotNull();
		assertThat(weatherController).isNotNull();
	}

	@Test
	public void securityPermissionsTest() throws Exception {
		String country = "AU";
		String city = "Sydney";
		String invalidCity = "Sy";
		String apiKeyValue = Constants.SAMPLE_API_KEYs[0];
		Optional<String> apiKey = Optional.of(apiKeyValue);
		Optional<String> invalidApiKey = Optional.of("TEST");

		/**
		 * Checks all possible apiKey input methods
		 */
		assertThat(weatherController.getWeatherForTheCity(country, city, null, apiKey)).isNotNull();
		assertThat(weatherController.getWeatherForTheCity(country, city, apiKey, null)).isNotNull();

		/**
		 * Checks the returning Exceptions due to improper inputs
		 */
		assertThrows(AccessDeniedException.class, () -> {
			weatherController.getWeatherForTheCity(country, city, null, null);
		});
	}

	@Test
	public void weatherForecastTest() throws Exception {
		String country = "AU";
		String city = "Sydney";
		String invalidCity = "Sy";
		String apiKeyValue = Constants.SAMPLE_API_KEYs[0];
		Optional<String> apiKey = Optional.of(apiKeyValue);

		/**
		 * Checks whether the correct input and output delivers correct result
		 */
		CityWeatherStatusDto weatherObj = weatherService.findWeatherForCity(country, city);
		assertThat(weatherObj).isNotNull();
		assertThat(weatherObj.getDescription()).isNotNull();
		assertTrue(weatherObj.getCountry().equals(country));
		assertTrue(weatherObj.getCity().equalsIgnoreCase(city));

		System.out.println("<ApplicationTest> Tests processing");
		System.out.println("<ApplicationTest> Weather Description: " + weatherObj.getDescription());
		System.out.println("<ApplicationTest> Api Key: " + apiKeyValue);

		/**
		 * Checks all possible apiKey input methods
		 */
		assertThat(weatherController.getWeatherForTheCity(country, city, null, apiKey)).isNotNull();
		assertThat(weatherController.getWeatherForTheCity(country, city, apiKey, null)).isNotNull();

		/**
		 * Checks the returning Exceptions due to improper inputs
		 */
		assertThrows(BadRequestException.class, () -> {
			weatherController.getWeatherForTheCity(country, invalidCity, null, apiKey);
		});
	}
}
