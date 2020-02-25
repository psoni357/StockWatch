package com.paavansoni.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private final List<Stock> stockList = new ArrayList<>();

    private RecyclerView recyclerView; // Layout's recyclerview

    private StockAdapter mAdapter; // Data to recyclerview adapter

    private SwipeRefreshLayout swiper; // The SwipeRefreshLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        for (int i = 0; i < 20; i++) {
            stockList.add(new Stock());
        }
    }

    private void doRefresh() {
        Collections.shuffle(stockList);
        mAdapter.notifyDataSetChanged();
        swiper.setRefreshing(false);
        Toast.makeText(this, "List content shuffled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        Toast.makeText(this, "You made a short click", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "This should add a new stock", Toast.LENGTH_LONG).show();
        return true;
    }
}
