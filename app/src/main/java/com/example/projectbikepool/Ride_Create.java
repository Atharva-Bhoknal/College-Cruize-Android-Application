package com.example.projectbikepool;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.projectbikepool.databinding.ActivityRideCreateBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class Ride_Create extends FragmentActivity  {

    private GoogleMap mMap;
    private ActivityRideCreateBinding binding;

    String live_location;
    FusedLocationProviderClient client;

    private SupportMapFragment mapFragment;
    private  int REQUEST_CODE = 111;
    public String Ulatitude, Ulongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRideCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.createridemap);
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();


        if (ActivityCompat.checkSelfPermission(Ride_Create.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Ride_Create.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }


    }

    private void getCurrentLocation() {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task<Location> task = client.getLastLocation();


            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {

                                //this is for getting current location
                                LatLng latLng1= new LatLng(location.getLatitude(), location.getLongitude());

                                double Ulatt = location.getLatitude();
                                double Ulongi = location.getLongitude();

                                Ulatitude = String.valueOf(Ulatt);    // string for storing value of user lattitude (Current Live)
                                Ulongitude = String.valueOf(Ulongi);  // string for storing value of user longitute (Current Live)

                                //showUlocation.setText("Your Location: " + "\n" + "Latitude: " + Ulatitude + "\n" + "Longitude: " + Ulongitude);
                                // To display langitute and lattitube on the screen

                                //make textview (id as a "showUlocation")in xml and find view by id in java

                                //  https://www.tutorialspoint.com/how-to-get-current-location-latitude-and-longitude-in-android  this website used as a reference for getting current location values

                                LatLng latLng= new LatLng(location.getLatitude(), location.getLongitude());

                                live_location = String.valueOf(latLng);

                                MarkerOptions markerOptions= new MarkerOptions().position(latLng).title("You Are Here");

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));

                                Objects.requireNonNull(googleMap.addMarker(markerOptions)).showInfoWindow();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode==REQUEST_CODE){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }


        }



}

