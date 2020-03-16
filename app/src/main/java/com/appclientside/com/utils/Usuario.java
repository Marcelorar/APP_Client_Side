package com.appclientside.com.utils;

import java.util.Objects;

public class Usuario {
    private String nombre;
    private String correo;
    private String telefono;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return nombre.equals(usuario.nombre) &&
                correo.equals(usuario.correo) &&
                telefono.equals(usuario.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, correo, telefono);
    }


}
