package com.example.acasasar.firebaseandmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.indigitall.pushsdk.Indigitall;
import net.indigitall.pushsdk.listeners.DeviceStatusListener;
import net.indigitall.pushsdk.listeners.ReadyListener;
import net.indigitall.pushsdk.model.DataModel;
import net.indigitall.pushsdk.model.PushModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker addressMarker;

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };

    // private static final String SENDER_ID = "289620728882";
    private static final String SENDER_ID = "24912286054";
    private static final String APPLICATION_ID = "9458402015adf51d5576a67.36040602";
    private static final String MAIN_CLASS_NAME = "MapsActivity";
    private static final String USE_EXTERNAL_APPS = "false";

    PushModel push = null;

    Indigitall indigitall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ContextCompat.checkSelfPermission(this, "true");

        checkPermissions();

        // TODO: Indigitall
        indigitall = new Indigitall(MapsActivity.this, SENDER_ID,
                APPLICATION_ID, MAIN_CLASS_NAME, USE_EXTERNAL_APPS);

        indigitall.initialize();

        indigitall.setIcon(R.drawable.common_google_signin_btn_icon_light);
        indigitall.setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        indigitall.setSmallIconColor("#FF6600");

        indigitall.setReadyListener(new ReadyListener() {
            @Override
            public void onReady(DataModel data) {
                Log.d("Indigitall", data.toString());
            }
        });

        indigitall.enableLocation(this);

        indigitall.enableDevice(this);

        indigitall.checkLocationStatus(this);

        DeviceStatusListener statusListener = new DeviceStatusListener() {
            @Override
            public void onChangeDeviceStatus(boolean b) {
                if (b) {
                    Log.d("Indigitall", "onChangeDeviceStatus true");
                } else {
                    Log.d("Indigitall", "onChangeDeviceStatus false");
                }
            }
        };

        indigitall.setDeviceStatusListener(statusListener);

        indigitall.setReadyListener(new ReadyListener() {
            @Override
            public void onReady(DataModel dataModel) {
                Log.d("Indigitall", dataModel.toString());
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Listeners
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng everis = new LatLng(40.4871332, -3.6701789);
        mMap.addMarker(new MarkerOptions().position(everis).title("Hola desde Everis!").draggable(true));

        CameraPosition camera = new CameraPosition.Builder()
                .target(everis)
                .zoom(15)           // limit -> 21
                .bearing(0)         // 0 - 365º
                .tilt(30)           // limit -> 90
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sevilla));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Click on: \n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lon: " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Long Click on: \n" +
                        "Lat: " + latLng.latitude + "\n" +
                        "Lon: " + latLng.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                String latitude = String.valueOf(marker.getPosition().latitude);
                String longitude = String.valueOf(marker.getPosition().longitude);

                Toast.makeText(getApplicationContext(), latitude + longitude, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(addressMarker != null)
            addressMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.common_google_signin_btn_icon_dark));
        addressMarker = mMap.addMarker(markerOptions);

    }


    /**
     * Checks the dynamically-controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                break;
        }
    }

}
