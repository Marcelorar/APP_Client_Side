package com.appclientside.com.utils;

import java.util.Objects;

public class WorkerLocation {
    private Posicion posicion;
    private Worker workUser;
    private float calificacion;
    private boolean visible;
    private boolean contratado;

    public WorkerLocation() {
    }

    public WorkerLocation(Posicion posicion, Worker workUser, float calificacion, boolean visible, boolean contratado) {
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
                contratado == that.contratado &&
                posicion.equals(that.posicion) &&
                workUser.equals(that.workUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posicion, workUser, calificacion, visible, contratado);
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

    public boolean isContratado() {
        return contratado;
    }

    public void setContratado(boolean contratado) {
        this.contratado = contratado;
    }
}
