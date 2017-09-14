package com.example.admin.dogrecognizer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;



public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.switcher);
        rotatingTextWrapper.setSize(24);

        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "Tô Minh Thành", "Võ Trần Thanh Lương");
        rotatable.setSize(24);
        rotatable.setAnimationDuration(500);

        rotatingTextWrapper.setContent("Created by ", rotatable);

    }
}
