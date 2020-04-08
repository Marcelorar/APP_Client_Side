package com.appclientside;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.appclientside.com.utils.MapWorkers;
import com.appclientside.com.utils.Pedido;
import com.appclientside.com.utils.Posicion;
import com.appclientside.com.utils.Profesion;
import com.appclientside.com.utils.Usuario;
import com.appclientside.com.utils.WorkerLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    protected LocationManager locationManager;
    private static final int PERMISSIONS = 0;
    private boolean posibleUbicar;
    private LatLng currentLocation;
    private Location initialLocation;
    private List<MapWorkers> workersInMap;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore db;
    private List<WorkerLocation> resAbatibleWorkers;
    private List<WorkerLocation> abatibleWorkers;
    private Handler handler;
    private Runnable runn;
    private int delay; //milliseconds
    private Usuario currentUser = new Usuario();
    private Usuario resCurrentUser;

    private String codeChat;
    private String resCodeChat;

    private boolean inicial;
    private boolean locked;

    private List<Profesion> profesions;
    private List<Profesion> fitProfesions;
    private Spinner filtro;

    private int currentWorkers;

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        filtro = findViewById(R.id.pFiltro);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(MapsActivity.this, "Permisos Necesarios :(!", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS);
                posibleUbicar = true;
            }
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        locked = false;

        //firebase
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("workers");
        db = FirebaseFirestore.getInstance();
        resAbatibleWorkers = new ArrayList<>();
        abatibleWorkers = new ArrayList<>();
        workersInMap = new ArrayList<>();
        inicial = false;
        handler = new Handler();
        delay = 500; //milliseconds
        initialLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //FLoating button de centrado

        FloatingActionButton fab = findViewById(R.id.centrador);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude()), 19.f));
            }
        });


        mAuth = FirebaseAuth.getInstance();
        getCurrentClient(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-"));
        profesions = new ArrayList<>();
        fitProfesions = new ArrayList<>();

        loadProfesions();
        runn = new Runnable() {
            public void run() {
                updateCurrentWorkersLocation();
                getCurrentClient(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-"));
                //Control de markers, no repetirlos
                Log.i("Error", workersInMap.size() + "");
                Log.i("Error2", abatibleWorkers.size() + "");
                if (!abatibleWorkers.isEmpty()) {
                    if (inicial && mMap != null) {
                        mMap.clear();
                        workersInMap = iniWorkersPosition();
                        inicial = false;
                    }
                    if (currentWorkers != abatibleWorkers.size()) {
                        inicial = true;
                    }
                    if (String.valueOf(filtro.getSelectedItem()).equals("Todas")) {
                        for (MapWorkers mw : workersInMap) {
                            mw.getMarker().setVisible(true);
                        }
                    } else {
                        for (MapWorkers mwj : workersInMap) {
                            mwj.getMarker().setVisible(true);
                        }
                        for (MapWorkers mw : workersInMap) {
                            if (!mw.getWorker().getWorkUser().getEspecializacion().equals(String.valueOf(filtro.getSelectedItem())))
                                mw.getMarker().setVisible(false);
                        }
                    }
                    for (MapWorkers wk : workersInMap) {
                        if (!wk.getMarker().getPosition().equals(new LatLng(wk.getWorker().getPosicion().getLatitude(),
                                wk.getWorker().getPosicion().getLongitude()))) {

                            animateMarker(wk.getMarker(), new LatLng(wk.getWorker().getPosicion().getLatitude(),
                                    wk.getWorker().getPosicion().getLongitude()), false);
                        }
                    }

                } else
                    inicial = true;

                if (currentUser.getContratando() != null && !currentUser.getContratando().isEmpty()) {
                    moveToProcess();
                }
                handler.postDelayed(this, delay);

            }
        };


        handler.postDelayed(runn, delay);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void saverProfesions() {
        profesions = fitProfesions;
    }

    private void loadProfesions() {
        myRef = database.getReference("profesions");
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> aux = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot profesion : dataSnapshot.getChildren()) {
                        Log.i("Test", profesion.getValue(Profesion.class).toString());
                        aux.add(profesion.getValue(Profesion.class).getNombre());
                        fitProfesions.add(profesion.getValue(Profesion.class));
                    }
                    aux.add(0, "Todas");
                    fitProfesions.add(0, new Profesion("Todas", "Green"));
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, aux);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    filtro.setAdapter(adapter);
                    saverProfesions();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        posibleUbicar = ((requestCode == PERMISSIONS) && (grantResults.length > 0)
                //2 tipos de ubicaciones permitidas
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED));
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude()), 19.f));

        //CONTRATAR
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (MapWorkers wm : workersInMap) {
                    if (wm.getMarker().getId().equals(marker.getId())) {
                        if (!wm.getWorker().getContratado().isEmpty() && wm.getWorker().isVisible()) {
                            Toast.makeText(MapsActivity.this, "Usuario negociando con otro cliente...", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Contratar");
                builder.setMessage("Me escoges a mi?");
                final Marker aux = marker;
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (MapWorkers wm : workersInMap) {
                            Log.i("Barrido:", wm.getWorker().getWorkUser().getNombre() + ">" + wm.getMarker().getId() + "<>" + aux.getId());
                            if (wm.getMarker().getId().equals(aux.getId())) {
                                registrarContratacion(wm.getWorker());
                                break;
                            }
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

    }

    private void registrarContratacion(final WorkerLocation w) {
        String pattern = "EEEEE MMMMM yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
        String date = simpleDateFormat.format(new Date());
        Pedido pAux = new Pedido(w, currentUser, date);
        db.collection("contratos")
                .add(pAux)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Guardado", "DocumentSnapshot written with ID: " + documentReference.getId());
                        hireWorker(w, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Guardado", "Error adding document", e);
                    }
                });
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
        updatePosition(location);
    }

    public List<MapWorkers> iniWorkersPosition(){
        List<MapWorkers> workersInMap = new ArrayList<>();
        currentWorkers = abatibleWorkers.size();
        for (WorkerLocation itr :abatibleWorkers) {
            workersInMap.add(
                    new MapWorkers(
                            mMap.addMarker(
                                    new MarkerOptions()
                                            .position(new LatLng(itr.getPosicion().getLatitude(), itr.getPosicion().getLongitude()))
                                            .title(itr.getWorkUser().getNombre() + " " + itr.getWorkUser().getApellido())
                                            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_obrero))
                            )
                    , itr));

        }
        return workersInMap;
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        Objects.requireNonNull(background).setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        Objects.requireNonNull(vectorDrawable).setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateCurrentWorkersLocation(){
        getAbatibleWorkers();
        List<WorkerLocation> wlcc;
        if(!abatibleWorkers.isEmpty()) {
            wlcc = abatibleWorkers;
            for (MapWorkers mw : workersInMap)
                for (WorkerLocation wl : wlcc) {
                    if (mw.getWorker().getWorkUser().getUsername().equals(wl.getWorkUser().getUsername()))
                        mw.setWorker(wl);
                }
        } else {
            //Marker que no se va correccion
            if (!workersInMap.isEmpty()) {
                workersInMap.clear();
                mMap.clear();
            }
        }
    }

    private void  saver(){
        abatibleWorkers = resAbatibleWorkers;
    }


    public void saverUser() {
        currentUser = resCurrentUser;
    }

    private void getCurrentClient(String userName) {
        myRef = database.getReference("clients");
        Query query = myRef.orderByChild("correo").equalTo(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    resCurrentUser = dataSnapshot.getChildren().iterator().next().getValue(Usuario.class);
                    Log.i("Current", resCurrentUser.getNombre());
                }
                saverUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getAbatibleWorkers() {
        myRef = database.getReference("workers");
        Query query = myRef;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    resAbatibleWorkers.clear();
                    for (DataSnapshot worker : dataSnapshot.getChildren()) {
                        if (Objects.requireNonNull(worker.getValue(WorkerLocation.class)).isVisible())
                            resAbatibleWorkers.add(worker.getValue(WorkerLocation.class));
                    }
                    saver();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    public void hireWorker(final WorkerLocation wLocation, final String code) {
        myRef = database.getReference("clients");
        myRef.child(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-")).child("contratando").setValue(code).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRef = database.getReference("workers");
                myRef.child(wLocation.getWorkUser().getUsername().replace("@", "+").replace(".", "-")).child("contratado").setValue(code).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
            }
        });
    }



    private void updatePosition(Location location) {
        myRef = database.getReference("clients");
        myRef.child(mAuth.getCurrentUser().getEmail().replace("@", "+").replace(".", "-"))
                .child("ubicacion").setValue(new Posicion(location.getLatitude(), location.getLongitude())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runn);
    }

    private void moveToProcess() {
        Intent intent = new Intent(this, OrderingProcess.class);
        intent.putExtra("orderCode", currentUser.getContratando());
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}
