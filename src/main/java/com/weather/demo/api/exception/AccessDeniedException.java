/**
 * Implements AccessDenicedException which is being used to throw when an unauthorised access request happens.
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */
package com.weather.demo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
