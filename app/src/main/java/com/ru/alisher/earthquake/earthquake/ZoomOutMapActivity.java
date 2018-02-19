package com.ru.alisher.earthquake.earthquake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ZoomOutMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mapReady = false;

    private MarkerOptions locationPin;

    private CameraPosition locationCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_out_map);

        String location = "";
        double lat = 0;
        double lon = 0;

        Intent intent = getIntent();

        if(intent.hasExtra(EarthquakeDetailActivity.LOCATION_EXTRA_KEY)) {
            location = intent.getStringExtra(EarthquakeDetailActivity.LOCATION_EXTRA_KEY);
        }

        if(intent.hasExtra(EarthquakeDetailActivity.LAT_EXTRA_KEY)) {
            lat = intent.getDoubleExtra(EarthquakeDetailActivity.LAT_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(EarthquakeDetailActivity.LON_EXTRA_KEY)) {
            lon = intent.getDoubleExtra(EarthquakeDetailActivity.LON_EXTRA_KEY, 0);
        }

        try {
            getSupportActionBar().setTitle(location);
        }
        catch (Exception e) {
        }

        setMap(lat, lon, location);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.addMarker(locationPin);

        flyTo(locationCameraPosition);
    }

    private void flyTo(CameraPosition target) {
        if(target != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }
    }

    private void setMap(double lat, double lon, String location) {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationPin = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(location);

        locationCameraPosition = CameraPosition.builder()
                .target(new LatLng(lat,lon))
                .zoom(7)
                .bearing(0)
                .tilt(45)
                .build();
    }
}
