/**
 * Implements a wind conditions embeddable entity object
 *
 * @Author Bandula Gamage
 * Date: 19/06/2024
 */
package com.weather.demo.api.model.dto.openweathersvc;

import javax.persistence.Embeddable;

@Embeddable
public class WindEmbeddable {

    private Float speed;

    private Float deg;

    public WindEmbeddable() {
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDeg() {
        return deg;
    }

    public void setDeg(Float deg) {
        this.deg = deg;
    }
}
