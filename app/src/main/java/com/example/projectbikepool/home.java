package com.example.projectbikepool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Objects;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ActionBar actionBar=getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#1E90FF"));
        assert actionBar != null;
        actionBar.setTitle("College Cruze PCCOER");
        actionBar.setBackgroundDrawable(colorDrawable);


        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


    }

    public void crt_ride(View view) {
        Intent intent = new Intent(home.this,Ride_Create.class);
        startActivity(intent);
    }

    public void visit_profile(View view) {
        Intent intent = new Intent(home.this,profile.class);
        startActivity(intent);
    }

    public void visit_book_ride(View view) {
        Intent intent = new Intent(home.this,Book_Ride.class);
        startActivity(intent);
    }

    public void req_visit(View view) {
        Intent intent = new Intent(home.this,Ride_requestes.class);
        startActivity(intent);
    }

    public void ride_details_open(View view) {
        Intent intent = new Intent(home.this,ride_details.class);
        startActivity(intent);
    }
    public void open_his(View view) {
        Intent intent = new Intent(home.this,History.class);
        startActivity(intent);
    }


}