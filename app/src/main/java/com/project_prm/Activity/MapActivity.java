package com.project_prm.Activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project_prm.R;
import com.project_prm.databinding.ActivityMapBinding;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((this));
        mapFragment.getMapAsync(MapActivity.this);

        binding.backBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(MapActivity.this);
        try {
            addressList = geocoder.getFromLocationName("đại học FPT TPHCM", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = addressList.get(0);
        if (address != null) {
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            myMap.addMarker(new MarkerOptions().position(latLng).title("đại học FPT TPHCM"));
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }
}