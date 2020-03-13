package com.appclientside.com.utils;

import java.util.Objects;

public class Posicion {
    private float latitude;
    private float longitude;

    public Posicion() {
    }

    public Posicion(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posicion)) return false;
        Posicion posicion = (Posicion) o;
        return Float.compare(posicion.latitude, latitude) == 0 &&
                Float.compare(posicion.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}

