package com.example.projectbikepool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Objects;

public class home extends AppCompatActivity {

    ImageView crtbtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        crtbtn=(ImageView) findViewById(R.id.crt);

        crtbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,Ride_Create.class);
                startActivity(intent);
            }
        });

    }
}