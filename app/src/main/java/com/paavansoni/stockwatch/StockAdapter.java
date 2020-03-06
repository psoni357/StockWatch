package com.paavansoni.stockwatch;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class StockAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private static final String TAG = "NotesAdapter";
    private List<Stock> stockList;
    private MainActivity mainAct;

    StockAdapter(List<Stock> empList, MainActivity ma) {
        this.stockList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Stock stock = stockList.get(position);

        holder.symbol.setText(stock.getSymbol());

        holder.name.setText(stock.getCompany());

        holder.price.setText(String.valueOf(stock.getLatestPrice()));

        String comboChange = (stock.getChange()) + "(" + stock.getChangePercentage() + ")";
        holder.percentage.setText(comboChange);

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
