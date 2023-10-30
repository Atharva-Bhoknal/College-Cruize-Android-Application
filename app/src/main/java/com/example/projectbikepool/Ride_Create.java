package com.example.projectbikepool;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Ride_Create extends FragmentActivity  {

    private GoogleMap mMap;
    String Upi;
    EditText upiid;
    double Ulatt,Ulongi;
    private ActivityRideCreateBinding binding;

    private Geocoder geocoder;
    private String destination;
    //spinner
    private Spinner locspinner;
    String[] pickuplocs= {"Select Location","Dharmaraj Chowk,Ravet,Pune","Ravet Chowk,Ravet,Pune", "Akurdi Railway Station,Akurdi,Pune", "Ravet Bridge,Akurdi,Pune"};
    private String pickuplocation;
//    private String pickuplocation;

    String live_location;
    FusedLocationProviderClient client;

    private SupportMapFragment mapFragment;
    private  int REQUEST_CODE = 111;
    public String Ulatitude, Ulongitude;

    private TimePicker artpk;
    String hrs,min,time;

    Button create_ride;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRideCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.createridemap);
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();



//        ################# GEO CODING ##############
        geocoder=new Geocoder(this);
        try {
            List<Address> addresses= geocoder.getFromLocation(Ulatt,Ulongi,1);
            if (addresses.size()>0)
            {
                StringBuilder stringBuilder= new StringBuilder();
                stringBuilder.append(addresses.get(0).getPostalCode()).append(addresses.get(0).getAdminArea()).append(addresses.get(0).getCountryName());
                destination=stringBuilder.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        upiid=findViewById(R.id.upiofuser);
        Upi=upiid.getText().toString();


//        ######### SPINNER CODE ##############


        locspinner=findViewById(R.id.spinnerlocation);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Ride_Create.this, android.R.layout.simple_spinner_item,pickuplocs);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        locspinner.setAdapter(adapter);

        locspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              pickuplocation= locspinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        create_ride=findViewById(R.id.crt_ride);
        create_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ############LOCATION MAP#######
//                String Source = "Your Location";
//                String Dest= pickuplocation;
//                Uri uri= Uri.parse("https://www.google.com/maps/dir/"+Source+"/"+Dest);
//                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//                intent.setPackage("com.google.android.apps.maps");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                Intent intent= new Intent(Ride_Create.this,direction.class);
                intent.putExtra("pickuplocation",pickuplocation);
                intent.putExtra("time",time);
                startActivity(intent);
            }
        });

        artpk=findViewById(R.id.arrivaltime);
        hrs= String.valueOf(artpk.getHour());
        min= String.valueOf(artpk.getMinute());
        time=""+hrs+":"+min;


        if (ActivityCompat.checkSelfPermission(Ride_Create.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Ride_Create.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }


    }




//    ######### CURRETNT LOCATION ############

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

                                Ulatt = location.getLatitude();
                                Ulongi = location.getLongitude();

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

