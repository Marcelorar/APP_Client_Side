package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class WorkerLocation implements Serializable {
    private Posicion posicion;
    private Worker workUser;
    private float calificacion;
    private boolean visible;
    private String contratado;

    public WorkerLocation() {
        workUser = new Worker();
        posicion = new Posicion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(posicion, workUser, calificacion, visible, contratado);
    }

    public WorkerLocation(Posicion posicion, Worker workUser, float calificacion, boolean visible, String contratado) {
        this.posicion = posicion;
        this.workUser = workUser;
        this.calificacion = calificacion;
        this.visible = visible;
        this.contratado = contratado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerLocation)) return false;
        WorkerLocation that = (WorkerLocation) o;
        return Float.compare(that.calificacion, calificacion) == 0 &&
                visible == that.visible &&
                posicion.equals(that.posicion) &&
                workUser.equals(that.workUser) &&
                contratado.equals(that.contratado);
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public Worker getWorkUser() {
        return workUser;
    }

    public void setWorkUser(Worker workUser) {
        this.workUser = workUser;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getContratado() {
        return contratado;
    }

    public void setContratado(String contratado) {
        this.contratado = contratado;
    }
}
