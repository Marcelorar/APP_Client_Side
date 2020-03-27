package com.appclientside.com.utils;

import java.util.Objects;

public class Usuario {
    private String nombre;
    private String correo;
    private Posicion ubicacion;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, Posicion ubicacion) {
        this.nombre = nombre;
        this.correo = correo;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Posicion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Posicion ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return nombre.equals(usuario.nombre) &&
                correo.equals(usuario.correo) &&
                ubicacion.equals(usuario.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, correo, ubicacion);
    }
}
