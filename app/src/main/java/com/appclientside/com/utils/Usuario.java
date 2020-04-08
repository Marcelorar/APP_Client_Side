package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
    private String nombre;
    private String correo;
    private Posicion ubicacion;
    private String contratando;

    public Usuario() {
        ubicacion = new Posicion();
    }

    public Usuario(String nombre, String correo, Posicion ubicacion, String contratando) {
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

    public String getContratando() {
        return contratando;
    }

    public void setContratando(String contratando) {
        this.contratando = contratando;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return nombre.equals(usuario.nombre) &&
                correo.equals(usuario.correo) &&
                ubicacion.equals(usuario.ubicacion) &&
                contratando.equals(usuario.contratando);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, correo, ubicacion, contratando);
    }
}
