package com.appclientside.com.utils;

import java.util.Objects;

public class Usuario {
    private String nombre;
    private String correo;
    private Posicion ubicacion;
    private boolean contratando;

    public Usuario() {
        ubicacion = new Posicion();
    }

    public Usuario(String nombre, String correo, Posicion ubicacion, boolean contratando) {
        this.nombre = nombre;
        this.correo = correo;
        this.ubicacion = ubicacion;
        this.contratando = contratando;
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

    public boolean isContratando() {
        return contratando;
    }

    public void setContratando(boolean contratando) {
        this.contratando = contratando;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return contratando == usuario.contratando &&
                nombre.equals(usuario.nombre) &&
                correo.equals(usuario.correo) &&
                ubicacion.equals(usuario.ubicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, correo, ubicacion, contratando);
    }
}
