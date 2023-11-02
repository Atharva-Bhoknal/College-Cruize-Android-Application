
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

public class History extends AppCompatActivity {


            FirebaseAuth firebaseAuth;
            FirebaseUser firebaseUser;
            LinearLayout linearLayout;
            FirebaseFirestore firestore ;
            Context context;
            TextView textview;

            CardView cardview;


            String email,name,mobile,pass_email,pass_name,pass_mob,pickuplocation,upi,dt,rider_name,rider_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

                ActionBar actionBar=getSupportActionBar();
                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E90FF"));
                assert actionBar != null;
                actionBar.setTitle("History");
                actionBar.setBackgroundDrawable(colorDrawable);



                firestore= FirebaseFirestore.getInstance();
                context = getApplicationContext();
                linearLayout = findViewById(R.id.linearLayout);
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                email=firebaseUser.getEmail();


                email=firebaseUser.getEmail();


                DocumentReference documentReferenc = firestore.collection("Completed Rides").document(email);
                documentReferenc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            pass_name = documentSnapshot.getString("Passenger Name");
                            pass_email = documentSnapshot.getString("Passenger Email");
                            pass_mob = documentSnapshot.getString("Passenger MobileNo");
                            pickuplocation = documentSnapshot.getString("Pick up Location");
                            rider_name=documentSnapshot.getString("Rider Name");
                            rider_mobile=documentSnapshot.getString("Rider MobileNo");
                            upi=documentSnapshot.getString("UPI ID");
                            dt=documentSnapshot.getString("Date");
                            Toast.makeText(context,""+email+""+name+""+mobile+""+pass_email+""+pass_name+""+pass_mob+""+pickuplocation,Toast.LENGTH_SHORT);
                            addDataToView(pass_name, pass_mob, pass_email, rider_name, email, rider_mobile, pickuplocation,dt);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


            }

            private void addDataToView(String name, String mobile, String email, String riderName, String riderEmail, String riderMob, String pickuplocation,String dt) {

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
                String text = "Passenger Name: " + name + "\nPassanger Email: " + email + "\nPassenger Mobile No:" + mobile +"\nRider Name : "+riderName+"\nRider Email : "+riderEmail+"\nRider Mobile No:"+riderMob +"\nPick up location : "+pickuplocation+"\nDate :"+dt;
                textview.setText(text);

                textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textview.setTextColor(Color.WHITE);
                textview.setPadding(25, 25, 25, 25);
                textview.setGravity(Gravity.CENTER);
                linearLayoutInner.addView(textview);
                linearLayoutInner.setLayoutParams(layoutparams);
                linearLayoutInner.setOrientation(LinearLayout.VERTICAL);

                cardview.addView(linearLayoutInner);
                linearLayout.addView(cardview);
            }
}