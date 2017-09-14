package com.example.admin.dogrecognizer.addDog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.dogrecognizer.addDog.data.PetContract.PetEntry;


/**
 * Created by Anku on 5/6/2017.
 */

/**
 * Database helper class for Pets app. Manages creation and version management.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    /** Name of the database file*/
    public static final String DATABASE_NAME = "shelter.db";

    /** Database version. If schema is changed then database version must be increased. */
    public static final int DATABASE_VERSION = 1;

    /**
     * Creates an instance of PetsDbHelper
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creates a String for CREATE TABLE command in SQLite
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL,"
                + PetEntry.COLUMN_PET_BREED + " TEXT,"
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL,"
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to do here as our database version is still 1
    }
}
