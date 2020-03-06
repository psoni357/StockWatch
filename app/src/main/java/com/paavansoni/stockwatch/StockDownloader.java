package com.paavansoni.stockwatch;

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

public class StockDownloader extends AsyncTask<String, Void, String> {
    private MainActivity mainactivity;

    private static final String TAG = "StockDownloaderAsync";

    private static final String DATA_URL_START = "https://cloud.iexapis.com/stable/stock/";

    private static final String DATA_URL_TOKEN = "/quote?token=pk_fb2f3ecacfe249efa26e398ad72a410d";

    StockDownloader(MainActivity ma){
        mainactivity = ma;
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);
        Stock stock = parseJSON(value);
        Log.d(TAG, "onPostExecute: ");
        mainactivity.acceptStock(stock);
        // Where you pass data back to your actiivyt
    }

    @Override
    protected String doInBackground(String... strings) {
        String symbol = strings[0];
        String builtURL = DATA_URL_START + symbol + DATA_URL_TOKEN;

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(builtURL);

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
        }
        catch (Exception e){
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return sb.toString();
    }

    private Stock parseJSON(String s) {
        Stock stock;
        try{
            JSONObject jObjMain = new JSONObject(s);
            String symbol = jObjMain.getString("symbol");
            String company = jObjMain.getString("companyName");
            double price = jObjMain.getDouble("latestPrice");
            double change = jObjMain.getDouble("change");
            double changePercent = jObjMain.getDouble("changePercent");

            stock = new Stock(symbol, company, price, change, changePercent);
        }
        catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
            stock = new Stock();
        }
        return stock;
    }
}
