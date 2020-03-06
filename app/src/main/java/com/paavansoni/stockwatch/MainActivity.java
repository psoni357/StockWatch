package com.paavansoni.stockwatch;


//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";

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
/*
        for (int i = 0; i < 20; i++) {
            stockList.add(new Stock("Symbol","name",i,0,0));
        }
 */
        Boolean connected = fillDB();

        databaseHandler = new DatabaseHandler(this);

        new NameDownloader(this).execute();
    }

    private void doRefresh() {
        Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();
    }

   public Boolean fillDB(){
        if (doNetCheck()){
            ArrayList<Stock> tempList = databaseHandler.loadStocks();
            for(Stock s:tempList){

            }
            return true;
       }
        else{
           return false;
       }

   }

    @Override
    public void onResume() {
        ArrayList<Stock> list = databaseHandler.loadStocks();

        stockList.clear();
        stockList.addAll(list);

        Log.d(TAG, "onResume: " + list);
        mAdapter.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        Toast.makeText(this, "You made a short click", Toast.LENGTH_SHORT).show();

        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);

        String site = siteStub + s.getSymbol();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(site));
        startActivity(i);
    }

    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        Toast.makeText(this, "You made a long click", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(symbolsReady==false){
            Toast.makeText(this, "Still downloading symbols. Please try again in a bit.", Toast.LENGTH_LONG).show();
        }
        return true;
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

    public void acceptSymbols(ArrayList<Stock> stocks) {
        stockSymbolList.clear();
        stockSymbolList.addAll(stocks);
        symbolsReady = true;
    }

    public void acceptStock(Stock stock) {

    }
}
