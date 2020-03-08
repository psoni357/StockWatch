package com.paavansoni.stockwatch;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;


public class StockDownloader extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainactivity;

    private static final String TAG = "StockDownloaderAsync";

    private static final String DATA_URL_START = "https://cloud.iexapis.com/stable/stock/";

    private static final String DATA_URL_TOKEN = "/quote?token=pk_fb2f3ecacfe249efa26e398ad72a410d";

    private Boolean foundNull = false;

    StockDownloader(MainActivity ma){
        mainactivity = ma;
    }

    @Override
    protected void onPostExecute(String value) {
        super.onPostExecute(value);
        if(foundNull){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainactivity);

            builder.setIcon(R.drawable.baseline_report_problem_black_48);

            builder.setMessage("No data found on stock " + value);
            builder.setTitle("Error");

            AlertDialog dialog = builder.create();
            dialog.show();
            mainactivity.doDelete(value);
        }
        else{
            Stock stock = parseJSON(value);
            Log.d(TAG, "onPostExecute: ");
            mainactivity.acceptStock(stock);
        }
        // Where you pass data back to your actiivyt
    }

    @Override
    protected String doInBackground(String... strings) {
        foundNull = false;
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
            foundNull = true;
            return symbol;
        }

        return sb.toString();
    }

    private Stock parseJSON(String s) {
        Stock stock = new Stock();
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
        }
        return stock;
    }
}
