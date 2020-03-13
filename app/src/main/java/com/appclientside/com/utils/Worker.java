package com.appclientside.com.utils;

import java.util.Objects;

public class Worker {
    private String username;
    private String Nombre;
    private String Apellido;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public Worker() {
    }

    public Worker(String username, String nombre, String apellido) {
        this.username = username;
        Nombre = nombre;
        Apellido = apellido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return username.equals(worker.username) &&
                Nombre.equals(worker.Nombre) &&
                Apellido.equals(worker.Apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, Nombre, Apellido);
    }
}

