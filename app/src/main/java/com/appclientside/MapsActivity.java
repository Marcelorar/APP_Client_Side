package com.appclientside;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.appclientside.com.utils.MapWorkers;
import com.appclientside.com.utils.Posicion;
import com.appclientside.com.utils.Worker;
import com.appclientside.com.utils.WorkerLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static final int PERMISSIONS = 0;
    private boolean posibleUbicar;
    private LatLng currentLocation;
    private Location initialLocation;
    private List<MapWorkers> workersInMap;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private List<WorkerLocation> resAbatibleWorkers;
    private List<WorkerLocation> abatibleWorkers;
    private Handler handler;
    private int delay; //milliseconds

    ////////// Test quemado
    //private Worker w ;
    //private WorkerLocation wl;
    //private MarkerOptions mPosition;
    ////-////

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS);
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        ////////// Test quemado
       //w = new Worker("88@ieee.com".replace("@","+").replace(".","-"),"Hector","Huerta");
        //wl = new WorkerLocation(new Posicion(-34, 151),w,true);
        ////-////

        //firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("workers");
        myRef.addValueEventListener(workersLocationListener);
        resAbatibleWorkers = new ArrayList<>();
        abatibleWorkers = new ArrayList<>();
       // myRef.child(wl.getWorkUser().getUsername()).setValue(wl);


         handler = new Handler();
        delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                updateCurrentWorkersLocation();
                if(!abatibleWorkers.isEmpty())
                for(MapWorkers wk:workersInMap) {
                    animateMarker(wk.getMarker(), new LatLng(wk.getWorker().getPosicion().getLatitude(),
                            wk.getWorker().getPosicion().getLongitude()), false);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);
        initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        //2 tipos de ubicaciones permitidas
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    posibleUbicar= true;
                } else {
                    posibleUbicar = false;
                }
                return;
            }
        }
    }

    ValueEventListener workersLocationListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            WorkerLocation actual = dataSnapshot.getValue(WorkerLocation.class);
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            //Log.w(TAG, "loadLocation:onCancelled", databaseError.toException());
            // ...
        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng( new LatLng(initialLocation.getLatitude(),initialLocation.getLongitude())));
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(),
        location.getLongitude());
    }

    public List<MapWorkers> iniWorkersPosition(){
        List<MapWorkers> workersInMap = new ArrayList<>();
        getAbatibleWorkers();
        if(!abatibleWorkers.isEmpty())
        for (WorkerLocation itr :abatibleWorkers) {
            workersInMap.add(new MapWorkers(mMap.addMarker(new MarkerOptions().position(new LatLng(itr.getPosicion().getLatitude(), itr.getPosicion().getLongitude())).title(itr.getWorkUser().getNombre()))
                    , itr));

        }
        return workersInMap;
    }


    private void updateCurrentWorkersLocation(){
        getAbatibleWorkers();
        List<WorkerLocation> wlcc;
        if(!abatibleWorkers.isEmpty()) {
            workersInMap = iniWorkersPosition();
            wlcc = abatibleWorkers;
            for (MapWorkers mw : workersInMap)
                for (WorkerLocation wl : wlcc) {
                    if (mw.getWorker().getWorkUser().getUsername().equals(wl.getWorkUser().getUsername()))
                        mw.setWorker(wl);
                }
        }
    }

    private void  saver(){
      abatibleWorkers = new ArrayList<>(resAbatibleWorkers);
    }

    private void getAbatibleWorkers() {
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    resAbatibleWorkers.clear();
                    for (DataSnapshot worker : dataSnapshot.getChildren()) {
                        resAbatibleWorkers.add(worker.getValue(WorkerLocation.class));
                    }
                    saver();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
