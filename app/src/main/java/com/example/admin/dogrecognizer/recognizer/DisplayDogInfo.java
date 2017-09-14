package com.example.admin.dogrecognizer.recognizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.admin.dogrecognizer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luongvo on 07/06/2017.
 */

public class DisplayDogInfo extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private List<DogInfo> dogInfos;
    private DogInfoAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_info);

        File pictureFile = (File) getIntent().getExtras().get("imgFile");
        Bitmap bm = null;
        if (pictureFile != null) {
            bm = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
        }
        ImageView imgv = (ImageView)findViewById(R.id.dogImg);
        imgv.setImageBitmap(bm);

        getSupportActionBar().setTitle("Result");
        Intent intent = getIntent();
        String jsonContent = intent.getStringExtra("jsonObject");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dogInfos = new ArrayList<>();

        Iterator<?> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String)keys.next();
            String val = null;
            try {
                val = jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            DogInfo dogInfo = new DogInfo();
            dogInfo.setTitle(key);
            dogInfo.setBody(val);
            dogInfos.add(dogInfo);
        }
        mRecyclerViewAdapter = new DogInfoAdapter(this, dogInfos);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);


    }
}
