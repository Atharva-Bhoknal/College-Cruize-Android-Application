package com.example.projectbikepool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String email_regex = "^[A-Za-z0-9+_.-]+@(.+)$";
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText name,email,mobile_no,pass;
    String NAME,EMAIL,MOBILE_NO,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText) findViewById(R.id.uname);
        email=(EditText) findViewById(R.id.signupemail);
        mobile_no=(EditText) findViewById(R.id.signup_mobile_no);
        pass=(EditText) findViewById(R.id.signpassword);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }
    public void sign_up(View view){
        NAME=name.getText().toString();
        EMAIL=email.getText().toString();
        MOBILE_NO=mobile_no.getText().toString();
        Password=pass.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("Name", NAME);
        user.put("Email", EMAIL);
        user.put("MobileNo", MOBILE_NO);
        user.put("Password", Password);

        if (!EMAIL.matches(email_regex)) {
            email.setError("Enter correct Email");
        }
        else if(!(MOBILE_NO.length()==10)){
            mobile_no.setError("Enter Correct Mobile Number");
        }
        else if (Password.length() < 6) {
            pass.setError("Password Length must be greater than 5");
        }
        else{
            db.collection("Users")
                    .document(""+EMAIL)
                    .set(user).
                    addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "Created Account Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
            firebaseAuth.createUserWithEmailAndPassword(EMAIL, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Can't Register!An Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}