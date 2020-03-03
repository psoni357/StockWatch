package com.paavansoni.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NameDownloader extends AsyncTask<String, Void, String> {
    private static final String DATA_URL =
            "https://api.iextrading.com/1.0/ref-data/symbolsl";

    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String TAG = "NameDownloaderAsync";

    NameDownloader(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);
        ArrayList<Stock> stocks = parseJSON(value);
        Log.d(TAG, "onPostExecute: ");
        mainActivity.acceptSymbols(stocks);
        // Where you pass data back to your actiivyt
    }

    @Override
    protected String doInBackground(String... strings) {
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }

    public ArrayList<Stock> parseJSON(String s) {
        ArrayList<Stock> parsedStocks = new ArrayList<>();
        try{
            JSONArray jObjMain = new JSONArray(s);
            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jStock = (JSONObject) jObjMain.get(i);
                String symbol = jStock.getString("symbol");
                String company = jStock.getString("name");

                parsedStocks.add(new Stock(symbol,company,0,0,0));
            }
        }
        catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return parsedStocks;
    }
}
