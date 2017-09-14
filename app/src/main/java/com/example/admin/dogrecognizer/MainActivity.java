package com.example.admin.dogrecognizer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.dogrecognizer.ListView.ListDog;
import com.example.admin.dogrecognizer.addDog.CatalogActivity;
import com.example.admin.dogrecognizer.recognizer.RecognizerMainActivity;
import com.example.admin.dogrecognizer.nearbyplaces.MapsActivity;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    Button recognizeButton;
    Button albumButton;
    Button aboutUsButton;
    Button mapButton;
    Button addDog;

    List<Button> mButtons = new ArrayList();
    private static final int MY_PERMISSION_READ_STORAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.example.admin.dogrecognizer", MODE_PRIVATE);

        askForPermission();

        recognizeButton = (Button)findViewById(R.id.recognize_button);
        albumButton =(Button)findViewById(R.id.album_button);
        aboutUsButton = (Button)findViewById(R.id.about_us_button);
        mapButton = (Button) findViewById(R.id.map_button);
        addDog = (Button) findViewById(R.id.adddog_button);

        // set Text for all buttons in main activity
        ViewGroup allView = (ViewGroup) findViewById(R.id.MainActivityLinearLayout);
        getAllButtons(allView);
       for (Button curInstance: mButtons) {
                Button tv_hello = (Button) findViewById(curInstance.getId());
                tv_hello.setTypeface(EasyFonts.captureIt(this));
        }

        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDogList = new Intent(MainActivity.this,
                        ListDog.class);
                startActivity(toDogList);
            }
        });
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAboutUs = new Intent(MainActivity.this,
                        AboutUs.class);
                startActivity(toAboutUs);

            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMap = new Intent(MainActivity.this,
                        MapsActivity.class);
                startActivity(toMap);
            }
        });
        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recognizeDog = new Intent(MainActivity.this,
                        RecognizerMainActivity.class);
                startActivity(recognizeDog);
            }
        });
        addDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDog = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(addDog);
            }
        });


    }

    void getAllButtons(ViewGroup v) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            if(child instanceof ViewGroup)
                getAllButtons((ViewGroup)child);
            else if(child instanceof Button)
                mButtons.add((Button)child);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) { // Check if first run

            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forBounds(new Rect(0,0,1000,500)," CHÀO MỪNG ĐẾN VỚI DOG RECOGNIZER").cancelable(false),
                            TapTarget.forView(findViewById(R.id.album_button), "Xem thư viện hình").transparentTarget(true).cancelable(false),
                            TapTarget.forView(findViewById(R.id.recognize_button), "Chụp và nhận diệns chó").transparentTarget(true).cancelable(false),
                            TapTarget.forView(findViewById(R.id.map_button), "bản đồ").transparentTarget(true).cancelable(false),
                            TapTarget.forView(findViewById(R.id.about_us_button), "Tác giả").transparentTarget(true).cancelable(false))
                   .listener(new TapTargetSequence.Listener() {
                        // This listener will tell us when interesting(tm) events happen in regards
                        // to the sequence
                        @Override
                        public void onSequenceFinish() {
                            // Yay
                        }

                        public void onSequenceStep(TapTarget lastTarget,boolean bool) {
                            // Perfom action for the current target
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            // Boo
                        }
                    }).start();

            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_READ_STORAGE);
        }
    }
}

