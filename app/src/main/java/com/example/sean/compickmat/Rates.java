package com.example.sean.compickmat;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Seán on 01/11/2015.
 */
public class Rates extends MainActivity {

    //Two popular currencies for proof of concept
    static double usd;
    static double gbp;

    public Rates() {
        //Query Fixer.io: JSON API for currency conversion.
        //Get the latest conversion rates for the above currencies.
        String stringUrl = "http://api.fixer.io/latest?symbols=USD,GBP";
        new DownloadWebpageTask().execute(stringUrl);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            //Create JSON Object and parse conversion rates
            JSONObject jsonObj = null;
            JSONObject rates = null;

            try {
                jsonObj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                rates = jsonObj.getJSONObject("rates");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                usd = rates.getDouble("USD");
                gbp = rates.getDouble("GBP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("GBP: "+gbp+", USD: "+usd);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 1000;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            //int response = conn.getResponseCode();
            //Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertStreamToString(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        // Reads data from the input stream until the buffer is full
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        // Stores the data
        StringBuilder sb = new StringBuilder();
        // Temp var holds each line
        String line = null;
        try {
            // Read in data from the buffer until empty
            while ((line = reader.readLine()) != null) {
                // Add line to end of string then add escape char
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

