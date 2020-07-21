package com.example.groomzoom;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    PlacesClient placesClient;
    LatLng latLng;
    LatLng facebookDefault = new LatLng(37.4846, -122.1495);
    String addressResult = "";
    Button btnSaveAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String apiKey = "AIzaSyB6IevBhLGUNcVKsfjQoik6MUArUV2RyEw";
        if(!Places.isInitialized())
            Places.initialize(getApplicationContext(), apiKey);
        btnSaveAddress = (Button) findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setVisibility(View.GONE);
        placesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latLng = place.getLatLng();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Address entered"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                addressResult = place.getAddress();
                btnSaveAddress.setVisibility(View.VISIBLE);
                addressResult = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("address", addressResult);
                setResult(23, i);

                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker where you are and move the camera
        LatLng location;
        if(latLng == null) {
            location = facebookDefault;
            btnSaveAddress.setVisibility(View.INVISIBLE);
        }
        else
            location = latLng;
        mMap.addMarker(new MarkerOptions().position(location).title("Address entered"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}