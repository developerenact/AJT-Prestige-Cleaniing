package com.android.ajtprestigecleaning.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle extras;
    CameraPosition camPos;
    CameraUpdate camUpd3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        extras = getIntent().getExtras();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(mMap.MAP_TYPE_NORMAL);
        try {
            if (extras.getString("lat") != null && extras.getString("lat").length() > 0) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(Float.parseFloat(extras.getString("lat")), Float.parseFloat(extras.getString("lng"))));
                googleMap.addMarker(marker);
                camPos = new CameraPosition.Builder()
                        .target(getCenterCoordinate())
                        .zoom(5)
                        .build();
                camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                mMap.animateCamera(camUpd3);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public LatLng getCenterCoordinate() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(Double.valueOf(extras.getString("lat")), Double.valueOf(extras.getString("lng"))));
        LatLngBounds bounds = builder.build();
        return bounds.getCenter();
    }

}
