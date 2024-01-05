package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUploadTask extends AsyncTask<String, Void, String> {
    private final String apiKey;
    private final ImageView imageView;
    private final TextView textView;

    private Drawable drawable;

    public ImageUploadTask(String apiKey, ImageView imageView, TextView textView) {
        this.apiKey = apiKey;
        this.imageView = imageView;
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        String imageUrl = params[0];
        String apiUrl = "https://api.logmeal.es/v2/image/recognition/complete"; // Replace with your API endpoint

        try {
            drawable = imageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                //Bitmap bitmap = downloadImage(imageUrl);
                String result = uploadImage(apiUrl, apiKey, bitmap);

                return result;
                // Now 'bitmap' contains the Bitmap from the ImageView
            } else {
                // Handle the case where the Drawable is not a BitmapDrawable
                Bitmap bitmap = downloadImage(imageUrl);
                String result = uploadImage(apiUrl, apiKey, bitmap);

                return result;
            }
            //Bitmap bitmap = downloadImage(imageUrl);

           // Bitmap bitmap = downloadImage(imageUrl);

           //String result = uploadImage(apiUrl, apiKey, bitmap);
            //return result;
        } catch (IOException | JSONException e) {
            return "Error: " + e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        // Update TextView with API response
        textView.setText(result);
    }

    private Bitmap downloadImage(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(imageUrl).build();
        try (Response response = client.newCall(request).execute()) {

            return BitmapFactory.decodeStream(response.body().byteStream());
        }
    }

    private String uploadImage(String apiUrl, String apiKey, Bitmap bitmap) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        // Convert bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Create multipart request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpeg", RequestBody.create(MediaType.parse("image/png"), byteArray))
                .addFormDataPart("apiKey", apiKey)
                .build();

        // Create request
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        // Execute request and get response
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            JSONObject recognitionResults = json.getJSONArray("recognition_results").getJSONObject(0);
            String name = recognitionResults.getString("name");
            return name.toString();

        }
    }
}
