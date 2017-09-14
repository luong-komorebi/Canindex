package com.example.admin.dogrecognizer.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

/**
 * Created by admin on 9/9/2017.
 */

public class utility {
    public Uri getResourceUri(Context context, int resourceID){
        Resources resources = context.getResources();
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceID))
                .appendPath(resources.getResourceTypeName(resourceID))
                .appendPath(resources.getResourceEntryName(resourceID))
                .build();
        return uri;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context inContext, Bitmap inImage) {
        Uri uri = getImageUri(inContext,inImage);
        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        cursor.close();
        return cursor.getString(idx);
    }
}

