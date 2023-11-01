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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ride_details extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    LinearLayout linearLayout;
    FirebaseFirestore firestore ;
    Context context;
    TextView textview;

    CardView cardview;

    SwipeRefreshLayout swipeRefreshLayout;
    String doc,email,name,mobile,rider_email,rider_name,rider_mob,pickuplocation;
    String data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2b87d9"));
        assert actionBar != null;
        actionBar.setTitle("Your Ride");
        actionBar.setBackgroundDrawable(colorDrawable);

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

        if (ContextCompat.checkSelfPermission(ride_details.this , android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ride_details.this , new String[]{Manifest.permission.SEND_SMS}, 100);
        }
        if (ContextCompat.checkSelfPermission(ride_details.this , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ride_details.this , new String[]{Manifest.permission.CALL_PHONE}, 100);
        }

        email=firebaseUser.getEmail();

        firestore.collection("Booked Rides").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        doc = documentChange.getDocument().getId();
                        data = data + " " + doc;

                        if(email.equals(documentChange.getDocument().getData().get("Passenger Email").toString())) {
                            name = documentChange.getDocument().getData().get("Passenger Name").toString();
                            mobile = documentChange.getDocument().getData().get("Passenger MobileNo").toString();
                            rider_name = documentChange.getDocument().getData().get("Rider Name").toString();
                            rider_email = documentChange.getDocument().getData().get("Rider Email").toString();
                            rider_mob = documentChange.getDocument().getData().get("Rider Mobile").toString();
                            pickuplocation = documentChange.getDocument().getData().get("Pick up Location").toString();

                            addDataToView(name, mobile, email, rider_name, rider_email, rider_mob, pickuplocation);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ride_details.this, "You don't have any rides", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void addDataToView(String name, String mobile, String email, String riderName, String riderEmail, String riderMob, String pickuplocation) {
        cardview = new CardView(getApplicationContext());
        LinearLayout linearLayoutInner = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparams.setMargins(10, 15, 10, 15);
        linearLayoutInner.setBackground(getDrawable(R.drawable.cardview_bg));
        cardview.setLayoutParams(layoutparams);
        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
        cardview.setMaxCardElevation(30);
        cardview.setMaxCardElevation(6);
        textview = new TextView(getApplicationContext());
        textview.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        textview.setLayoutParams(layoutparams);
        String text = "Passenger Name: " + name + "\nPassanger Email: " + email + "\nPassenger Mobile No:" + mobile +"\nRider Name : "+riderName+"\nRider Email : "+riderEmail+"\nRider Mobile No:"+riderMob +"Pick up location : "+pickuplocation;
        textview.setText(text);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textview.setTextColor(Color.WHITE);
        textview.setPadding(25, 25, 25, 25);
        textview.setGravity(Gravity.CENTER);
        linearLayoutInner.addView(textview);

        Button call = new Button(getApplicationContext());
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    String url="tel:"+riderMob.toString();
                    //        Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse(url));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(context, "An Error Occurred Please Check Entered Number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        call.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        delete.setLayoutParams(layoutparams);
        call.setText("CALL");
        call.setLayoutParams(layoutparams);
        call.setHapticFeedbackEnabled(true);

        call.setBackground(getDrawable(R.drawable.button_design));

        call.setLetterSpacing(0.2f);
        call.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        Button payment = new Button(getApplicationContext());
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        payment.setLayoutParams(layoutparams);
        payment.setText("Make Payment");
        payment.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        payment.setLayoutParams(layoutparams);
        payment.setHapticFeedbackEnabled(true);
        payment.setBackground(getDrawable(R.drawable.button_design));
        payment.setPadding(25,0,25,0);
        payment.setLetterSpacing(0.2f);
        LinearLayout linearbtn = new LinearLayout(getApplicationContext());

        linearbtn.addView(call);
        linearbtn.addView(payment);
        linearLayoutInner.setLayoutParams(layoutparams);
        linearbtn.setLayoutParams(layoutparams);
        linearLayoutInner.addView(linearbtn);
        linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
        cardview.addView(linearLayoutInner);

        linearLayout.addView(cardview);

    }



}