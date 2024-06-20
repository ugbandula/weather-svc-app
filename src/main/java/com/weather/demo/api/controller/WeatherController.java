/**
 * Implements a RESTful controller to deliver weather information
 *
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */
package com.weather.demo.api.controller;

import com.weather.demo.api.exception.AccessDeniedException;
import com.weather.demo.api.exception.BadRequestException;
import com.weather.demo.api.exception.TooManyRequestException;
import com.weather.demo.api.throttler.UsagePlanService;
import com.weather.demo.api.service.WeatherService;
import com.weather.demo.util.Constants;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping( value = Constants.API_ROOT + "/weather" )
public class WeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private UsagePlanService usagePlanService;

    @GetMapping(path="/{country}/{city}")
    public ResponseEntity<Object> getWeatherForTheCity(
            @PathVariable String country,
            @PathVariable String city,
            @RequestHeader(value = "apiKey") Optional<String> apiKey,
            @RequestParam(value = "apiKey") Optional<String> apiKeyQueryParam) throws Exception {

        System.out.println("<WeatherController> Inside");
        if (!StringUtils.hasText(country))
            throw new BadRequestException("Invalid parameter - Country not found");
        if (country.length() < 2)
            throw new BadRequestException("Invalid parameter - Invalid country found");

        if (!StringUtils.hasText(city))
            throw new BadRequestException("Invalid parameter - City not found");
        if (city.length() < 3)
            throw new BadRequestException("Invalid parameter - Invalid city found");

        String extractedApiKey = null;
        if (apiKey != null && apiKey.isPresent()) {
            extractedApiKey = apiKey.get();
        } else if (apiKeyQueryParam != null && apiKeyQueryParam.isPresent()) {
            extractedApiKey = apiKeyQueryParam.get();
        }

        if (!StringUtils.hasText(extractedApiKey))
            throw new AccessDeniedException("Access denied - API Key not found");

        Bucket bucket = usagePlanService.resolveBucket(extractedApiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        // Maximum usage has reached for that API key. Need to wait the timeout to serve again
        if (!probe.isConsumed()) {
            throw new TooManyRequestException("Too many requests within the given period. Please retry in a while.");
        }

        LOGGER.info("[WeatherController] Reading weather data for {}, {}", country, city);
        return ResponseEntity.status(HttpStatus.OK).body(weatherService.findWeatherForCity(country, city));
    }

}
