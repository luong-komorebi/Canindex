package com.example.admin.dogrecognizer.ListView;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MyImage {
    private String title, description;
    Uri uri;



    public MyImage(String title, String description, Uri uri) {
        this.title = title;
        this.description = description;
        this.uri = uri;
    }



    public Uri getUri(){ return this.uri;}

    public String getTitle() { return title; }
    public String getDescription() { return description; }



    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }



}