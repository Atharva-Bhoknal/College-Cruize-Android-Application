package com.example.projectbikepool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectbikepool.databinding.ActivityBookRideBinding;
import com.example.projectbikepool.databinding.ActivityRideCreateBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Book_Ride extends AppCompatActivity {
    private int REQUEST_CODE = 111 ;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore ;
    String live_location;
    private ActivityBookRideBinding binding;
    double Ulatt,Ulongi;

    private SupportMapFragment mapFragment;

    public String Ulatitude, Ulongitude;
    Context context;
    private Spinner locspinner;
    CardView cardview;
    LinearLayout.LayoutParams cvlayoutparams ,tvlayoutparams ;
    TextView textview;
    String documentID;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    String doc,email,mobile,name,time,rider_name,rider_email,rider_mobile;
    String[] pickuplocs= {"Select Location","Dharmaraj Chowk,Ravet,Pune","Ravet Chowk,Ravet,Pune", "Akurdi Railway Station,Akurdi,Pune", "Ravet Bridge,Akurdi,Pune"};
    private String pickuplocation;
    String data="";
    FusedLocationProviderClient client;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_ride);

        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E90FF"));
        assert actionBar != null;
        actionBar.setTitle("Book Ride");
        actionBar.setBackgroundDrawable(colorDrawable);

        binding = ActivityBookRideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bookridemap);
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        swipeRefreshLayout = findViewById(R.id.refreshlayout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        firestore= FirebaseFirestore.getInstance();
        context = getApplicationContext();
        linearLayout = findViewById(R.id.linearLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email=firebaseUser.getEmail();


        if (ContextCompat.checkSelfPermission(Book_Ride.this , android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Book_Ride.this , new String[]{Manifest.permission.SEND_SMS}, 100);
        }

        if (ActivityCompat.checkSelfPermission(Book_Ride.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Book_Ride.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }


        email=firebaseUser.getEmail();
        try {
            DocumentReference documentReference = firestore.collection("Users").document(email);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        name = documentSnapshot.getString("Name");
                        mobile = documentSnapshot.getString("MobileNo");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
        }
        locspinner=findViewById(R.id.spinnerlocation);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Book_Ride.this, android.R.layout.simple_spinner_item,pickuplocs);
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

    private void addDataToView(String name, String riderEmail, String riderMobile, String time) {

        cardview = new CardView(getApplicationContext());
        LinearLayout linearLayoutInner = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparams.setMargins(10, 15, 10, 15);

        cardview.setLayoutParams(layoutparams);
        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
        cardview.setCardBackgroundColor(Color.parseColor("#36BFB1"));
        cardview.setMaxCardElevation(30);
        cardview.setMaxCardElevation(6);
        textview = new TextView(getApplicationContext());
        textview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textview.setLayoutParams(layoutparams);
        String text = "Name: " + rider_name + "\nRider Email: " + riderEmail + "\nRider Mobile No:" + riderMobile + "\nTime of arrival: " + time ;
        textview.setText(text);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textview.setTextColor(Color.WHITE);
        textview.setPadding(25, 25, 25, 25);
        textview.setGravity(Gravity.CENTER);
        linearLayoutInner.addView(textview);

        Button book = new Button(getApplicationContext());
        book.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        delete.setLayoutParams(layoutparams);
        book.setText("Book");
        book.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Rides Available")
                        .whereEqualTo("Location",pickuplocation )
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentID = documentSnapshot.getId();

                                    Map<String, Object> ord_data = new HashMap<>();
                                    ord_data.put("Passenger Name", name);
                                    ord_data.put("Passenger MobileNo", mobile);
                                    ord_data.put("Passenger Email", email);
                                    ord_data.put("Rider Name", rider_name);
                                    ord_data.put("Rider Email", riderEmail);
                                    ord_data.put("Rider MobileNo", riderMobile);
                                    ord_data.put("Pick up Location", pickuplocation);


                                    firestore.collection("Requests")
                                            .document(riderEmail)
                                            .set(ord_data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Booking Confirm", Toast.LENGTH_SHORT).show();
                                                                    try {
                                                                        String msg = "Hey " + rider_name + " I am waiting for you at "+pickuplocation+" please accept my booking.";
                                                                        SmsManager smgr = SmsManager.getDefault();
                                                                        smgr.sendTextMessage(riderMobile, null, msg, null, null);
                                                                        Toast.makeText(Book_Ride.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(Book_Ride.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    recreate();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });



                                } else {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                Toast.makeText(context, ""+data, Toast.LENGTH_SHORT).show();
            }
        });


        linearLayoutInner.addView(book);
        linearLayoutInner.setLayoutParams(layoutparams);
        linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
        cardview.addView(linearLayoutInner);
        linearLayout.addView(cardview);
    }


    public void search_rides(View view) {
        try {

            firestore.collection("Rides Available").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    try {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            doc = documentChange.getDocument().getId();
                            data = data + " " + doc;
                            if(pickuplocation.equals(documentChange.getDocument().getData().get("Location").toString())) {
                                rider_name = documentChange.getDocument().getData().get("Name").toString();
                                rider_email = documentChange.getDocument().getData().get("Email").toString();
                                rider_mobile = documentChange.getDocument().getData().get("MobileNo").toString();
                                time = documentChange.getDocument().getData().get("Time").toString();

                                addDataToView(name, rider_email, rider_mobile, time);
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(Book_Ride.this, "An error Occured!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(context, "No Rides Available", Toast.LENGTH_SHORT).show();
        }

    }
}