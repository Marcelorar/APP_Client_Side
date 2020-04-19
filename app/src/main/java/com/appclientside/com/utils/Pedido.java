package com.appclientside.com.utils;

import java.io.Serializable;
import java.util.Objects;

public class Pedido implements Serializable {
    private WorkerLocation worker;
    private Usuario client;
    private String timestamp;
    private Posicion destino;

    public Pedido() {
        worker = new WorkerLocation();
        client = new Usuario();
        destino = new Posicion();
    }

    public Pedido(WorkerLocation worker, Usuario client, String timestamp, Posicion destino) {
        this.worker = worker;
        this.client = client;
        this.timestamp = timestamp;
        this.destino = destino;
    }

    public WorkerLocation getWorker() {
        return worker;
    }

    public void setWorker(WorkerLocation worker) {
        this.worker = worker;
    }

    public Usuario getClient() {
        return client;
    }

    public void setClient(Usuario client) {
        this.client = client;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Posicion getDestino() {
        return destino;
    }

    public void setDestino(Posicion destino) {
        this.destino = destino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return worker.equals(pedido.worker) &&
                client.equals(pedido.client) &&
                timestamp.equals(pedido.timestamp) &&
                destino.equals(pedido.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worker, client, timestamp, destino);
    }
}

