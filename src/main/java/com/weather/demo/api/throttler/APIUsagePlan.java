/**
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */

package com.weather.demo.api.throttler;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

/**
 * Defines and controls the API usage for a given user.
 * There are two types of users are available.<br>
 * <ol>
 *     <li>User with unlimited capacity (rather 1000 requests per hour)</li>
 *     <li>User with limited capacity (5 requests per hour)</li>
 * </ol>
 */
public enum APIUsagePlan {
    UNLIMITED_USER(1000),
    RESTRICTED_USER(5);

    private int bucketCapacity;

    private APIUsagePlan(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    /**
     * The available capacity
     * @return Bandwidth for the user
     */
    public Bandwidth getLimit() {
        return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofHours(1)));
    }

    public int bucketCapacity() {
        return bucketCapacity;
    }

    public static APIUsagePlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return UNLIMITED_USER;
        } else if (apiKey.equals(RESTRICTED_USER)) {
            return RESTRICTED_USER;
        }
        return UNLIMITED_USER;
    }

}
