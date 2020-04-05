package com.appclientside.com.utils;

import com.google.android.gms.maps.model.Marker;

import java.util.Objects;

public class MapWorkers {
    private Marker marker;
    private WorkerLocation worker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public WorkerLocation getWorker() {
        return worker;
    }

    public void setWorker(WorkerLocation worker) {
        this.worker = worker;
    }

    public MapWorkers() {
        worker = new WorkerLocation();
    }

    public MapWorkers(Marker marker, WorkerLocation worker) {
        this.marker = marker;
        this.worker = worker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapWorkers)) return false;
        MapWorkers that = (MapWorkers) o;
        return marker.equals(that.marker) &&
                worker.equals(that.worker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marker, worker);
    }
}
