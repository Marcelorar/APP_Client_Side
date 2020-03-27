package com.appclientside.com.utils;

import java.util.Objects;

public class Locker {
    private Usuario cliente;
    private Worker worker;

    public Locker() {
    }

    public Locker(Usuario cliente, Worker worker) {
        this.cliente = cliente;
        this.worker = worker;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locker)) return false;
        Locker locker = (Locker) o;
        return cliente.equals(locker.cliente) &&
                worker.equals(locker.worker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, worker);
    }

    @Override
    public String toString() {
        return cliente.getCorreo() +
                ";" + worker.getUsername();
    }
}
