package com.example.projectbikepool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class splash extends AppCompatActivity {

    private  static int SPLASH_SCREEN = 3000;
    Animation fade;

    ImageView image;

    LottieAnimationView lot1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);

//        image = findViewById(R.id.image1);
//        lot1 = findViewById(R.id.lot);

//        image.setAnimation(fade);
//        lot1.setAnimation(fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}