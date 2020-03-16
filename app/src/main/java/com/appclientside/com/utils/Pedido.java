package com.appclientside.com.utils;

import java.util.Objects;

public class Pedido {
    private WorkerLocation worker;
    private Usuario client;
    private String timestamp;

    public Pedido() {
    }

    public Pedido(WorkerLocation worker, Usuario client, String timestamp) {
        this.worker = worker;
        this.client = client;
        this.timestamp = timestamp;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return worker.equals(pedido.worker) &&
                client.equals(pedido.client) &&
                timestamp.equals(pedido.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worker, client, timestamp);
    }
}
