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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    int Total_Price;
    String doc,email,name,mobile,rider_email,rider_name,rider_mob,pickuplocation,upi_id,dt;
    String data="";
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2b87d9"));
        assert actionBar != null;
        actionBar.setTitle("Your Ride");
        actionBar.setBackgroundDrawable(colorDrawable);


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
                            upi_id = documentChange.getDocument().getData().get("UPI ID").toString();
                            dt = documentChange.getDocument().getData().get("Date").toString();

                            addDataToView(name, mobile, email, rider_name, rider_email, rider_mob, pickuplocation,dt);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ride_details.this, "You don't have any rides", Toast.LENGTH_SHORT).show();
                }
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
        //linearLayoutInner.setBackground(getDrawable(R.drawable.cardview_bg));
        cardview.setCardBackgroundColor(Color.parseColor("#4dafe8"));

        LinearLayout.LayoutParams layoutparamscardview = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparamscardview.setMargins(15, 15, 15, 15);


        cardview.setLayoutParams(layoutparamscardview);
        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
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

      //  Button call = new Button(getApplicationContext());
        MaterialButton call = new MaterialButton(this);
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
        call.setIcon(ContextCompat.getDrawable(this,R.drawable.baseline_call_24));

        call.setBackgroundColor(getColor(R.color.dark_cyan));
        call.setLetterSpacing(0.2f);
        call.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        MaterialButton payment = new MaterialButton(this);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upi_pay();
                payUsingUpi();
            }
        });
        payment.setLayoutParams(layoutparams);
        payment.setText("Make Payment");
        payment.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        payment.setLayoutParams(layoutparams);
        payment.setHapticFeedbackEnabled(true);
        payment.setIcon(ContextCompat.getDrawable(this,R.drawable.baseline_payment_24));

        payment.setBackgroundColor(getColor(R.color.dark_cyan));
        payment.setPadding(25,0,25,0);
        payment.setLetterSpacing(0.2f);
        LinearLayout linearbtn = new LinearLayout(getApplicationContext());

//        linearbtn.addView(call);
//        linearbtn.addView(payment);
        linearLayoutInner.addView(call);
        linearLayoutInner.addView(payment);
        linearLayoutInner.setLayoutParams(layoutparams);
        linearbtn.setLayoutParams(layoutparams);
        linearLayoutInner.addView(linearbtn);
        linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
        cardview.addView(linearLayoutInner);

        linearLayout.addView(cardview);

    }



    //UPI Payment
    void payUsingUpi() {
        if(pickuplocation.equals("Dharmaraj Chowk,Ravet,Pune"))
        {
            Total_Price=25;
        }
        else if(pickuplocation.equals("Ravet Chowk,Ravet,Pune"))
        {
            Total_Price=5;
        }
        else if(pickuplocation.equals("Akurdi Railway Station,Akurdi,Pune"))
        {
            Total_Price=30;
        }
        else if(pickuplocation.equals("Ravet Bridge,Akurdi,Pune"))
        {
            Total_Price=15;
        }


        String amount=String.valueOf(Total_Price);

        Uri uri = Uri.parse("upi://pay").buildUpon()
//                .appendQueryParameter("pa", "atharvvkale22@oksbi")
                .appendQueryParameter("pa", upi_id)
                .appendQueryParameter("pn",rider_name)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay using");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(ride_details.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(ride_details.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(ride_details.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
//                pay_on_delivery();
                upi_pay();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(ride_details.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ride_details.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ride_details.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void upi_pay() {
        if(pickuplocation.equals("Dharmaraj Chowk,Ravet,Pune"))
        {
            Total_Price=25;
        }
        else if(pickuplocation.equals("Ravet Chowk,Ravet,Pune"))
        {
            Total_Price=5;
        }
        else if(pickuplocation.equals("Akurdi Railway Station,Akurdi,Pune"))
        {
            Total_Price=30;
        }
        else if(pickuplocation.equals("Ravet Bridge,Akurdi,Pune"))
        {
            Total_Price=15;
        }
        String t=String.valueOf(Total_Price);
        SimpleDateFormat formatter = new SimpleDateFormat("E dd/MM/yyyy 'at' hh:mm:ss a");
        Date date = new Date();
        String dt= formatter.format(date);

        Map<String, Object> ord_data = new HashMap<>();
        ord_data.put("Passenger Name", name);
        ord_data.put("Passenger MobileNo", mobile);
        ord_data.put("Passenger Email", email);
        ord_data.put("Rider Name", rider_name);
        ord_data.put("Rider Email", rider_mob);
        ord_data.put("Rider MobileNo", rider_email);
        ord_data.put("Pick up Location", pickuplocation);
        ord_data.put("TotalPrice", t);
        ord_data.put("Date", dt);
        ord_data.put("PayementMode","UPI");
        ord_data.put("UPI ID", upi_id);



            try {
                String msg = "Hey :"+name+" thanks for picking as rider. \nHope you have enjoyed your ride.\nHave a nice day!!";
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage("8080728482", null, msg, null, null);
                Toast.makeText(ride_details.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ride_details.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
            }

            firestore.collection("Completed Rides")
                    .document(rider_email)
                    .set(ord_data).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ride_details.this, "Destination Reached", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ride_details.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });

            //delete ride
        firestore.collection("Booked Rides")
                .document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                                                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        recreate();
//                                                    startActivity(new Intent(ViewAllStock.this , MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to Confirm Server Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}