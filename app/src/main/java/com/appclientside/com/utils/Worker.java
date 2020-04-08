package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class Worker implements Serializable {
    private String username;
    private String Nombre;
    private String Apellido;
    private String foto;
    private String especializacion;

    public Worker() {
    }

    public Worker(String username, String nombre, String apellido, String foto, String especializacion) {
        this.username = username;
        Nombre = nombre;
        Apellido = apellido;
        this.foto = foto;
        this.especializacion = especializacion;
    }

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Worker)) return false;
        Worker worker = (Worker) o;
        return username.equals(worker.username) &&
                Nombre.equals(worker.Nombre) &&
                Apellido.equals(worker.Apellido) &&
                foto.equals(worker.foto) &&
                especializacion.equals(worker.especializacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, Nombre, Apellido, foto, especializacion);
    }
}

