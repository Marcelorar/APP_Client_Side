package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class Posicion implements Serializable {
    private double latitude;
    private double longitude;

    public Posicion() {
    }

    public Posicion(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicion)) return false;
        Posicion posicion = (Posicion) o;
        return Double.compare(posicion.latitude, latitude) == 0 &&
                Double.compare(posicion.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}

