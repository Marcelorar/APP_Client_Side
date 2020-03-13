package com.appclientside.com.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class WorkerLocation {
    private Posicion posicion;
    private Worker workUser;
    private boolean visible;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerLocation)) return false;
        WorkerLocation that = (WorkerLocation) o;
        return visible == that.visible &&
                posicion.equals(that.posicion) &&
                workUser.equals(that.workUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posicion, workUser, visible);
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public WorkerLocation() {
    }

    public WorkerLocation(Posicion posicion, Worker workUser, boolean visible) {
        this.posicion = posicion;
        this.workUser = workUser;
        this.visible = visible;
    }
}
