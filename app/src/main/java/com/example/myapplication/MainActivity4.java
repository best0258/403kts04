package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity4 extends AppCompatActivity {
    ImageView im5,im6,im7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


        im5 = findViewById(R.id.eathome4);
        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent7 = new Intent(getApplicationContext(),MainActivity5.class);
                startActivity(intent7);
            }
        });

        im6 = findViewById(R.id.chome4);
        im6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent8 = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent8);
            }
        });

        im7 = findViewById(R.id.sthome4);
        im7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent9 = new Intent(getApplicationContext(),MainActivity6.class);
                startActivity(intent9);
            }
        });
    }
}