package com.example.projectbikepool;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projectbikepool.databinding.ActivityDirectionBinding;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class direction extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String pickuplocation,time;
    Geocoder geocoder;
    private ActivityDirectionBinding binding;

    private Button Directions;

    private TextView dest,arrtime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDirectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pickuplocation=getIntent().getStringExtra("pickuplocation");
        time=getIntent().getStringExtra("time");

        geocoder=new Geocoder(this);

        Directions=findViewById(R.id.directionbtn);
        Directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Source = "Your Location";
                String Dest= pickuplocation;
                Uri uri = Uri.parse("https://www.google.com/maps/dir/"+Source+"/"+Dest);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        arrtime=findViewById(R.id.arrivaltimetv);
        dest=findViewById(R.id.destinationtv);

        arrtime.setText(time);
        dest.setText(pickuplocation);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.directionmap);
        mapFragment.getMapAsync(this);
    }

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

        try {
            List<Address> addresses=geocoder.getFromLocationName(pickuplocation,1);
            Address address= addresses.get(0);
            LatLng destination= new LatLng(address.getLatitude(),address.getLongitude());
            MarkerOptions markerOptions= new MarkerOptions().position(destination).title("Your Destination");
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination,17));
            Objects.requireNonNull(googleMap.addMarker(markerOptions)).showInfoWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}