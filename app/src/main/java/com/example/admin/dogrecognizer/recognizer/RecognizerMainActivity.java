package com.example.admin.dogrecognizer.recognizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.admin.dogrecognizer.R;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecognizerMainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }



    private void initComponent() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(30, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();



        // Change base URL to your upload server URL.
        service = new Retrofit.Builder().baseUrl(getString(R.string.server_url)).client(client).build().create(Service.class);
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image From");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            final ProgressDialog progressDialog = new ProgressDialog(RecognizerMainActivity.this);
            progressDialog.setMessage("Processing...");
            progressDialog.show();

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null) return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);

            makeRequest(file, progressDialog);
        }
    }

    private void makeRequest(final File file, final ProgressDialog progressDialog) {

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        Call<ResponseBody> req = service.postImage(body, name);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String json = null;
                try {
                    json = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String jsonGet = json;
                //Toast.makeText(MainActivity.this, "Here is your dog prediction: "+json, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
//                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                        alertDialog.setTitle("Result");
//                        alertDialog.setMessage("Click to see your result");
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent intent = new Intent(MainActivity.this, DisplayDogInfo.class);
//                                intent.putExtra("jsonObject", jsonGet);
//                                intent.putExtra("imgFile", file);
//                                startActivity(intent);
//                            }
//                        });
//                        alertDialog.show();
                Intent intent = new Intent(RecognizerMainActivity.this, DisplayDogInfo.class);
                intent.putExtra("jsonObject", jsonGet);
                intent.putExtra("imgFile", file);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (t instanceof SocketTimeoutException) {
                    progressDialog.dismiss();
                    Toast.makeText(RecognizerMainActivity.this, "Timeout. Check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public File getFile() {
//        return this.file;
//    }
}
