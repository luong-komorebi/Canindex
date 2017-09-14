package com.example.admin.dogrecognizer.ListView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ImageDatabase";

    // Table Names
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_uri";
    private static final String KEY_DESC = "image_desc";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " BLOB,"+
            KEY_DESC + " DESC);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating table
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(db);
    }

    public void addEntry(ArrayList<MyImage> images) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (MyImage image : images
                    ) {
                ContentValues cv = new ContentValues();
                cv.put(KEY_NAME, image.getTitle());
                cv.put(KEY_IMAGE, image.getUri().toString());
                cv.put(KEY_DESC, image.getDescription());
                database.insertOrThrow(DB_TABLE, null, cv);
                Log.v("insert to sqlite", "succeded");
            }
        }catch(Exception e){
            Log.e("Problem",e+"");
        }

    }
    public ArrayList<MyImage> returnImage(){
        ArrayList<MyImage> result = new ArrayList<MyImage>();
        String selectQuery = "SELECT  * FROM " + DB_TABLE;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Uri uri = Uri.parse(cursor.getString(1));
                String desc = cursor.getString(2);
                result.add(new MyImage(name,desc,uri));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }
}