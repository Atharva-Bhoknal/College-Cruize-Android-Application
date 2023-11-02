package com.example.projectbikepool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Ride_requestes extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    LinearLayout linearLayout;
    FirebaseFirestore firestore ;
    Context context;
    TextView textview;

    CardView cardview;


    String email,name,mobile,pass_email,pass_name,pass_mob,pickuplocation,upi,dt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_requestes);

        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E90FF"));
        assert actionBar != null;
        actionBar.setTitle("Pickup requests");
        actionBar.setBackgroundDrawable(colorDrawable);



        firestore= FirebaseFirestore.getInstance();
        context = getApplicationContext();
        linearLayout = findViewById(R.id.linearLayout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        email=firebaseUser.getEmail();


        if (ContextCompat.checkSelfPermission(Ride_requestes.this , android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Ride_requestes.this , new String[]{Manifest.permission.SEND_SMS}, 100);
        }

        email=firebaseUser.getEmail();


            DocumentReference documentReferenc = firestore.collection("Requests").document(email);
            documentReferenc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        pass_name = documentSnapshot.getString("Passenger Name");
                        pass_email = documentSnapshot.getString("Passenger Email");
                        pass_mob = documentSnapshot.getString("Passenger MobileNo");
                        pickuplocation = documentSnapshot.getString("Pick up Location");
                        name=documentSnapshot.getString("Rider Name");
                        mobile=documentSnapshot.getString("Rider MobileNo");
                        upi=documentSnapshot.getString("UPI ID");
                        dt=documentSnapshot.getString("Date");
                        Toast.makeText(context,""+email+""+name+""+mobile+""+pass_email+""+pass_name+""+pass_mob+""+pickuplocation,Toast.LENGTH_SHORT);
                        addDataToView(email,name,mobile,pass_email,pass_name,pass_mob,pickuplocation,upi,dt);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });


    }

    private void addDataToView(String email, String name, String mobile, String passEmail, String passName, String passMob, String pickuplocation,String upi,String dt) {

        cardview = new CardView(getApplicationContext());
        LinearLayout linearLayoutInner = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparams.setMargins(10, 15, 10, 15);

        LinearLayout.LayoutParams layoutparamscardview = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparamscardview.setMargins(15, 15, 15, 15);


        cardview.setLayoutParams(layoutparamscardview);
        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
        cardview.setCardBackgroundColor(Color.parseColor("#2b87d9"));
        cardview.setMaxCardElevation(30);
        cardview.setMaxCardElevation(6);
        textview = new TextView(getApplicationContext());
        textview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textview.setLayoutParams(layoutparams);
        String text = "Passanger Name: " + passName + "\nPassanger Email: " + passEmail + "\nMobile No:" + passMob + "\nPick up location : "+pickuplocation;
        textview.setText(text);

        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textview.setTextColor(Color.WHITE);
        textview.setPadding(25, 25, 25, 25);
        textview.setGravity(Gravity.CENTER);
        linearLayoutInner.addView(textview);

        MaterialButton delete = new MaterialButton(this);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                                    Map<String, Object> ord_data = new HashMap<>();
                                    ord_data.put("Passenger Name", pass_name);
                                    ord_data.put("Passenger MobileNo", pass_mob);
                                    ord_data.put("Passenger Email", pass_email);
                                    ord_data.put("Rider Name", name);
                                    ord_data.put("Rider Email", email);
                                    ord_data.put("Rider Mobile", mobile);
                                    ord_data.put("Pick up Location", pickuplocation);
                                    ord_data.put("UPI ID", upi);
                                    ord_data.put("Date", dt);


                                    firestore.collection("Booked Rides")
                                            .document(email)
                                            .set(ord_data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Booking Confirm", Toast.LENGTH_SHORT).show();
                                                    firestore.collection("Rides Available")
                                                            .document(email)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
//                                                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                                                    try {
                                                                        String msg = "Hey " + pass_name + " I coming to pick you at "+pickuplocation+". I will be there in a moment.";
                                                                        SmsManager smgr = SmsManager.getDefault();
                                                                        smgr.sendTextMessage(pass_mob, null, msg, null, null);
                                                                        Toast.makeText(Ride_requestes.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(Ride_requestes.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    recreate();
//                                                    startActivity(new Intent(ViewAllStock.this , MainActivity.class));
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, "Failed to Confirm Server Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    firestore.collection("Requests")
                                                            .document(email)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(Ride_requestes.this, "Confirmed Pickup", Toast.LENGTH_SHORT).show();
//
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, "Failed to Confirm Server Error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
        });

        delete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        delete.setLayoutParams(layoutparams);
        delete.setText("Confirm");
        delete.setIcon(ContextCompat.getDrawable(this,R.drawable.baseline_check_box_24));
        delete.setBackgroundColor(getColor(R.color.dark_cyan));
        delete.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);


        MaterialButton Cancel = new MaterialButton(this);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Requests")
                        .document(email)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Ride_requestes.this, "Pickup cancelled", Toast.LENGTH_SHORT).show();
                                try {
                                    String msg = "Hey " + pass_name + " sorry I cant pick you.";
                                    SmsManager smgr = SmsManager.getDefault();
                                    smgr.sendTextMessage(pass_mob, null, msg, null, null);
                                    Toast.makeText(Ride_requestes.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(Ride_requestes.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to Confirm Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        Cancel.setLayoutParams(layoutparams);
        Cancel.setText("Cancel");
        Cancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        Cancel.setIcon(ContextCompat.getDrawable(this,R.drawable.baseline_cancel_24));
        Cancel.setBackgroundColor(getColor(R.color.dark_cyan));

        linearLayoutInner.addView(Cancel);
        linearLayoutInner.addView(delete);
        linearLayoutInner.setLayoutParams(layoutparams);
        linearLayoutInner.setOrientation(LinearLayout.VERTICAL);

        cardview.addView(linearLayoutInner);
        linearLayout.addView(cardview);
    }
}