package com.example.admin.dogrecognizer.ListView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.admin.dogrecognizer.Helper.utility;
import com.example.admin.dogrecognizer.R;

import java.io.File;
import java.util.ArrayList;

public class ListDog extends AppCompatActivity {

    private ArrayList<String> dogname = new ArrayList<String>();
    private ArrayList<String> desc = new ArrayList<String>();
    ArrayList<Integer> imgid = new ArrayList<Integer>();
    private ArrayList<MyImage> images = new ArrayList<MyImage>();

    private CustomListView customListview;
    private ListView lst;
    private DatabaseHelper database;

    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    void purgeDirectory(File dir) {
        for (File file: dir.listFiles()) {
            if (file.isDirectory()) purgeDirectory(file);
            file.delete();
        }
        Log.v("delete all files", "successfully");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_list);

        database = new DatabaseHelper(getApplicationContext());
        images = database.returnImage();


        if( images.size()==0 ) { // if no item exists in sqlite database, add default images
            dogname.add("Berger Picard");
            dogname.add("Akita");
            dogname.add("American Foxhound");
            dogname.add("American Eskimo Dog");

            desc.add("desc1");
            desc.add("desc2");
            desc.add("desc3");
            desc.add("desc4");

            imgid.add(R.drawable.berger_picard);
            imgid.add(R.drawable.akita);
            imgid.add(R.drawable.american_foxhound);
            imgid.add(R.drawable.american_eskimo_dog);
            for (int i = 0; i < 4; i++) {

                images.add(new MyImage(dogname.get(i), desc.get(i),
                        new utility().getResourceUri(getApplicationContext(),imgid.get(i))));

            }
            Log.v("initializing", "default_images_loaded");
        }


        lst = (ListView) findViewById(R.id.listview);
        customListview = new CustomListView(this,images);
        lst.setAdapter(customListview);
    }

    public void btnAddOnClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.setTitle("Alert Dialog View");
        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnChoosePath).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activeGallery();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activeTakePhoto();
                dialog.dismiss();
            }
        });

        // show dialog on screen
        dialog.show();
        CustomListView customListview = new CustomListView(this,images);
        customListview.notifyDataSetChanged();
    }

    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if(cursor!=null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        //default name and description, remember to let users define them
                        dogname.add("aDog" + ( images.size() + 1));
                        desc.add("Testing description");
                        Bitmap img = BitmapFactory.decodeFile(picturePath);
                        MyImage image = new MyImage(dogname.get(dogname.size()-1),desc.get(desc.size()-1),
                                new utility().getImageUri(getApplicationContext(),img));
                        images.add(image);
                    }

                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(mCapturedImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    cursor.close();

                    dogname.add("aDog" + ( images.size() + 1));
                    desc.add("Testing description");
                    Bitmap img = BitmapFactory.decodeFile(picturePath);
                    MyImage image = new MyImage(dogname.get(dogname.size()-1),desc.get(desc.size()-1),
                            new utility().getImageUri(getApplicationContext(),img));
                    images.add(image);
                }
        }
        database.addEntry(images);
        customListview.notifyDataSetChanged();
    }

}
