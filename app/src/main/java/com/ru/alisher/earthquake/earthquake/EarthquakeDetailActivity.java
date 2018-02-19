package com.ru.alisher.earthquake.earthquake;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ru.alisher.earthquake.earthquake.helper.GeolocationDecimalToDegreesConverter;
import com.ru.alisher.earthquake.earthquake.helper.Helper;
import com.ru.alisher.earthquake.earthquake.network.NetworkUtils;

import java.util.Date;

public class EarthquakeDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String MAGNITUDE_EXTRA_KEY = "magnitude";
    public static final String LOCATION_EXTRA_KEY = "location";
    public static final String OFFSET_LOCATION_EXTRA_KEY = "offsetLocation";
    public static final String LAT_EXTRA_KEY = "latitude";
    public static final String LON_EXTRA_KEY = "longitude";
    public static final String TIME_EXTRA_KEY = "time";
    public static final String MERCALLI_SCALE_EXTRA_KEY = "mercalliScale";
    public static final String ALERT_LEVEL_EXTRA_KEY = "alertLevel";

    private GoogleMap mMap;
    private boolean mapReady = false;

    private String location = "";
    private double lat = 0;
    private double lon = 0;

    private MarkerOptions locationPin;

    private CameraPosition locationCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        double magnitude = 0;
        String offsetLocation = "";
        long timeInMilliseconds = 0;
        double mercalliIntensityScale = 0;
        String alertLevel = "";

        Intent intent = getIntent();

        if(intent.hasExtra(MAGNITUDE_EXTRA_KEY)) {
            magnitude = intent.getDoubleExtra(MAGNITUDE_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(LOCATION_EXTRA_KEY)) {
            location = intent.getStringExtra(LOCATION_EXTRA_KEY);
        }

        if(intent.hasExtra(OFFSET_LOCATION_EXTRA_KEY)) {
            offsetLocation = intent.getStringExtra(OFFSET_LOCATION_EXTRA_KEY);
        }

        if(intent.hasExtra(LAT_EXTRA_KEY)) {
            lat = intent.getDoubleExtra(LAT_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(LON_EXTRA_KEY)) {
            lon = intent.getDoubleExtra(LON_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(TIME_EXTRA_KEY)) {
            timeInMilliseconds = intent.getLongExtra(TIME_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(MERCALLI_SCALE_EXTRA_KEY)) {
            mercalliIntensityScale = intent.getDoubleExtra(MERCALLI_SCALE_EXTRA_KEY, 0);
        }

        if(intent.hasExtra(ALERT_LEVEL_EXTRA_KEY)) {
            alertLevel = intent.getStringExtra(ALERT_LEVEL_EXTRA_KEY);
        }

        try {
            getSupportActionBar().setTitle(location);
        }
        catch (Exception e) {
        }

        TextView magnitudeTextView = findViewById(R.id.magnitude);
        TextView locationTextView = findViewById(R.id.location);
        TextView timeTextView = findViewById(R.id.time);
        TextView mercalliScaleTextView = findViewById(R.id.mercalliScale);
        TextView latLonTextView = findViewById(R.id.latLon);
        TextView alertLevelTextView = findViewById(R.id.alertLevel);

        magnitudeTextView.setText(Double.toString(magnitude));
        locationTextView.setText((offsetLocation + location));

        Date dateObject = new Date(timeInMilliseconds);
        timeTextView.setText((Helper.formatDate(dateObject) + ", " + Helper.formatTime(dateObject)));

        mercalliScaleTextView.setText(Double.toString(mercalliIntensityScale));
        latLonTextView.setText(GeolocationDecimalToDegreesConverter.convert(lat, lon));
        alertLevelTextView.setText(alertLevel);

        setMap(lat, lon, location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_SMALL
                || (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            getMenuInflater().inflate(R.menu.detail, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_zoom_out_map) {
            Intent zoomOutMapIntent = new Intent(this, ZoomOutMapActivity.class);
            zoomOutMapIntent.putExtra(LOCATION_EXTRA_KEY, location);
            zoomOutMapIntent.putExtra(LAT_EXTRA_KEY, lat);
            zoomOutMapIntent.putExtra(LON_EXTRA_KEY, lon);

            startActivity(zoomOutMapIntent);

            return true;
        }
        else if (id == android.R.id.home) {
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
