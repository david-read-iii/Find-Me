package com.davidread.findme;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * {@link MapsActivity} represents a Google Maps user interface that keeps the user's current
 * location marked.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * Int constant for identifying a device permission request.
     */
    private final int REQUEST_LOCATION_PERMISSIONS = 0;

    /**
     * {@link GoogleMap} for accessing functions of the Google Maps API.
     */
    private GoogleMap mMap;

    /**
     * {@link FusedLocationProviderClient} for getting location updates.
     */
    private FusedLocationProviderClient mClient;

    /**
     * {@link LocationRequest} for getting the user's fine location.
     */
    private LocationRequest mLocationRequest;

    /**
     * {@link LocationCallback} for updating this activity's UI with the user's fine location.
     */
    private LocationCallback mLocationCallback;

    /**
     * Float for saving the zoom level of {@link #mMap} in {@link #onMapReady(GoogleMap)} and
     * restoring it in {@link #updateMap(Location)}.
     */
    private float mZoomLevel = 15;

    /**
     * Invoked once when this {@link MapsActivity} is initially created. It initializes this
     * activity's {@link SupportMapFragment} to asynchronously load and display a Google Map.
     * Then, it defines location objects for getting the user's location.
     */
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

        // Create location request.
        mLocationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create location callback.
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    updateMap(location);
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Invoked when this {@link MapsActivity} enters the foreground. It sets up {@link #mClient} to
     * provide location updates to this activity.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (hasLocationPermission()) {
            mClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback,
                    Looper.getMainLooper()
            );
        }
    }

    /**
     * Invoked when this {@link MapsActivity} leaves the foreground. It stops {@link #mClient} from
     * providing further location updates.
     */
    @Override
    public void onPause() {
        super.onPause();
        mClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * Invoked when a {@link GoogleMap} is ready to be used. It initializes {@link #mMap}, saves
     * the zoom level in {@link #mZoomLevel}, and sets a click listener for the markers.
     *
     * @param googleMap A {@link GoogleMap} associated with the {@link SupportMapFragment} that
     *                  defines the callback.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Save zoom level.
        mMap.setOnCameraMoveListener(() -> {
            CameraPosition cameraPosition = mMap.getCameraPosition();
            mZoomLevel = cameraPosition.zoom;
        });

        // Handle marker click.
        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(MapsActivity.this, "Lat: " + marker.getPosition().latitude +
                            System.getProperty("line.separator") + "Long: " + marker.getPosition().longitude,
                    Toast.LENGTH_LONG).show();
            return false;
        });
    }

    /**
     * Adds a new marker for the passed {@link Location} onto {@link #mMap} and moves the map
     * window over that marker. It removes all previously placed markers before doing this.
     *
     * @param location {@link Location} to mark on {@link #mMap}.
     */
    private void updateMap(Location location) {

        // Get current location.
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Place a marker at the current location.
        MarkerOptions myMarker = new MarkerOptions()
                .title("Here you are!")
                .position(myLatLng);

        // Remove previous marker.
        mMap.clear();

        // Add new marker.
        mMap.addMarker(myMarker);

        // Zoom to previously saved level.
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLatLng, mZoomLevel);
        mMap.animateCamera(update);
    }

    /**
     * Checks if the user has already allowed this app access to the device's fine location. If not,
     * it requests this permission.
     *
     * @return True if the user has already allowed the permission. False otherwise.
     */
    private boolean hasLocationPermission() {
        // Request fine location permission if not already granted.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS
            );
            return false;
        }

        return true;
    }
}