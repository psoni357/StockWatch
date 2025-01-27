package com.paavansoni.stockwatch;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    int pos;

    Stock s;


    private static final String siteStub = "http://www.marketwatch.com/investing/stock/";

    private final List<Stock> stockList = new ArrayList<>();

    private ArrayList<Stock> stockSymbolList = new ArrayList<>();

    private RecyclerView recyclerView; // Layout's recyclerview

    private StockAdapter mAdapter; // Data to recyclerview adapter

    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout

    private Boolean symbolsReady = false;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        symbolsReady = false;
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        mAdapter = new StockAdapter(stockList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        databaseHandler = new DatabaseHandler(this);

        setTitle("Stock Watch");

        if(!doNetCheck()){
            Toast.makeText(this, "No network connection. Please connect and refresh the app.", Toast.LENGTH_LONG).show();
            fillDBnoConn();
        }
        else{
            new NameDownloader(this).execute();
            fillDB();
        }
    }

    private void doRefresh() {
        if(doNetCheck()){
            symbolsReady = false;
            new NameDownloader(this).execute();
            fillDB();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.baseline_info_black_48);

            builder.setMessage("Stocks cannot be updated without a network connection.");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        swiper.setRefreshing(false);
    }

   public void fillDB(){
       stockList.clear();
       ArrayList<Stock> tempList = databaseHandler.loadStocks();
       for(Stock s:tempList){
           new StockDownloader(this).execute(s.getSymbol());
       }
   }

   public void fillDBnoConn(){
       stockList.clear();
       ArrayList<Stock> tempList = databaseHandler.loadStocks();
       stockList.addAll(tempList);
       updateList();
   }

    private void updateList() {
        Collections.sort(stockList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        pos = recyclerView.getChildLayoutPosition(v);
        s = stockList.get(pos);

        String site = siteStub + s.getSymbol();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(site));
        startActivity(i);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        pos = recyclerView.getChildLayoutPosition(v);
        s = stockList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.baseline_delete_black_48);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stockList.remove(pos);
                doDelete(s.getSymbol());
                updateList();
                Toast.makeText(MainActivity.this, "Stock deleted!", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //not doing anything since not cancelled
            }
        });

        builder.setMessage("Would you like to delete stock " + s.getSymbol() + "?");
        builder.setTitle("Confirm Delete");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!doNetCheck()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.baseline_info_black_48);

            builder.setMessage("Stocks cannot be added without a network connection.");
            builder.setTitle("No Network Connection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(!symbolsReady){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.baseline_info_black_48);

            builder.setMessage("Stocks cannot be added without downloading stock symbols. Please refresh the app.");
            builder.setTitle("Stock Symbols Not Downloaded");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            // Single input value dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Create an edittext and set it to be the builder's view
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            et.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);

            builder.setIcon(R.drawable.baseline_info_black_48);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //search for stock
                    findStock(et.getText().toString());
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //cancelled
                }
            });

            builder.setMessage("Please enter a Stock Symbol:");
            builder.setTitle("Stock Selection");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    public Boolean dupFound(Stock st){
        for(Stock s:stockList){
            if(s.getSymbol().equals(st.getSymbol())){
                return true;
            }
        }
        return false;
    }

    public void dupMsg(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.baseline_report_problem_black_48);

        builder.setMessage("Stock " + symbol + " is already in the list.");
        builder.setTitle("Duplicate Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void findStock(String stock){
        ArrayList<String> found = new ArrayList<>();
        final ArrayList<Stock> corrStock = new ArrayList<>();

        for (Stock s : stockSymbolList){
            if (s.getSymbol().contains(stock.toUpperCase()) || s.getCompany().contains(stock.toUpperCase())){
                found.add(s.getSymbol() + " - " + s.getCompany());
                corrStock.add(s);
            }
        }

        final CharSequence[] sArray = new CharSequence[found.size()];
        for (int i = 0; i < found.size(); i++)
            sArray[i] = found.get(i);

        if(found.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.baseline_report_problem_black_48);

            builder.setMessage("No data found for stock symbol.");
            builder.setTitle("Symbol Not Found: " + stock);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (found.size()==1){
            Stock a = corrStock.get(0);
            if(dupFound(a)){
                dupMsg(a.getSymbol());
            }
            else{
                getStock(a);
                //Toast.makeText(this, "Stock Added!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make a selection");
            builder.setIcon(R.drawable.baseline_info_black_48);

            // Set the builder to display the string array as a selectable
            // list, and add the "onClick" for when a selection is made
            builder.setItems(sArray, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //picked an option
                    Stock a = corrStock.get(which);
                    if(dupFound(a)){
                        dupMsg(a.getSymbol());
                    }
                    else{
                        getStock(a);
                    }
                }
            });

            builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //hit nevermind so nothing happens
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }

    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }

    public void doAdd(Stock s){
        databaseHandler.addStock(s);
    }

    public void doDelete(String s){
        databaseHandler.deleteStock(s);
    }

    public void getStock(Stock s){
        //this should pull stock information from Async and add it
        Toast.makeText(this, "Stock Added!", Toast.LENGTH_SHORT).show();
        new StockDownloader(this).execute(s.getSymbol());
        doAdd(s);
    }

    public void acceptSymbols(ArrayList<Stock> stocks) {
        stockSymbolList.clear();
        stockSymbolList.addAll(stocks);
        symbolsReady = true;
    }

    public void acceptStock(Stock stock) {
        stockList.add(stock);
        updateList();
    }
}
