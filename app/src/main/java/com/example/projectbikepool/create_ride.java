package com.example.projectbikepool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class create_ride extends AppCompatActivity {

    Spinner vehile_type;
    String[] vtypes = { "Vegetables", "Grains", "Pulses", "Fruits","Soyabeans", "Masoordal", "GreenPeas", "Mungdaal", "Turdal","ChanaDal","BlackedEyedPeas","Chickpeas"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        vehile_type=(Spinner)findViewById(R.id.vehicle_type);
        ArrayAdapter<String> Category = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, vtypes);
        Category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehile_type.setAdapter(Category);
    }
}