package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class Profesion implements Serializable {
    private String nombre;
    private String color;

    public Profesion() {
    }

    public Profesion(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profesion)) return false;
        Profesion profesion = (Profesion) o;
        return nombre.equals(profesion.nombre) &&
                color.equals(profesion.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, color);
    }

    @Override
    public String toString() {
        return "Profesion{" +
                "nombre='" + nombre + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
