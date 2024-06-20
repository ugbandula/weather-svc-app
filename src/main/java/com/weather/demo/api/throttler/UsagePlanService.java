/**
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */

package com.weather.demo.api.throttler;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements an API usage plan service using the defined limits.
 */
@Service
public class UsagePlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsagePlanService.class);

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Returned the resolved bucket for the given API Key
     * @param apiKey
     * @return
     */
    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    /**
     * Generates a new Bucket if required
     * @param apiKey
     * @return
     */
    private Bucket newBucket(String apiKey) {
        APIUsagePlan apiUsagePlan = APIUsagePlan.resolvePlanFromApiKey(apiKey);
        return Bucket4j.builder()
                .addLimit(apiUsagePlan.getLimit())
                .build();
    }

}
