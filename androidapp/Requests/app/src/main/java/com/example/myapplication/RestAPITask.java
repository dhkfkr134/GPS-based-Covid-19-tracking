package com.example.myapplication;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestAPITask extends AsyncTask<Integer, Void, Void> {
    // Variable to store url
    protected String mURL;

    // Constructor
    public RestAPITask(String url) {
        mURL = url;
    }

    // Background work
    protected Void doInBackground(Integer... params) {
//        String result = null;

        try {
            // Open the connection
            URL url = new URL(mURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept",
                    "application/vnd.github.v3+json");
            conn.setRequestProperty("Contact-Me",
                    "hathibelagal@example.com");
            //응답 확인
            if (conn.getResponseCode() == 200) {
                Log.d("response","응답성공"+conn.getResponseCode());
            } else {
                Log.d("response","응답실패"+conn.getResponseCode());
            }

            InputStream responseBody = conn.getInputStream();
            InputStreamReader responseBodyReader =
                    new InputStreamReader(responseBody, "UTF-8");
            //json 응답 파싱
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals("organization_url")) { // Check if desired key
                    // Fetch the value as a String
                    String value = jsonReader.nextString();

                    // Do something with the value
                    // ...

                    break; // Break out of the loop
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }
            jsonReader.close();
            Log.d("responsebody", String.valueOf(jsonReader));
            Log.d("responsebody1", String.valueOf(responseBody));

            //어떻게 쓰는건지 모름
//            // Get the stream
//            StringBuilder builder = new StringBuilder();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
//            String line;
//            String test = "test";
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//            // Set the result
//            result = builder.toString();
//
//
        }
        catch (Exception e) {
            // Error calling the rest api
            Log.e("REST_API", "GET method failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    protected void onProgressUpdate(Void... values){

    }
    protected void onPostExecute(Void values){

    }
}
