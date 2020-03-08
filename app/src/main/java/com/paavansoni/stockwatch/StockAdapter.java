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

        String arrow;
        if(stock.getChange()>0){
            arrow = "\u25B2";
            holder.symbol.setTextColor(0xFF4CAF50);
            holder.name.setTextColor(0xFF4CAF50);
            holder.price.setTextColor(0xFF4CAF50);
            holder.percentage.setTextColor(0xFF4CAF50);
        }
        else if(stock.getChange()<0){
            arrow = "\u25BC";
            holder.symbol.setTextColor(0xFFD11616);
            holder.name.setTextColor(0xFFD11616);
            holder.price.setTextColor(0xFFD11616);
            holder.percentage.setTextColor(0xFFD11616);
        }
        else{
            arrow = "";
            holder.symbol.setTextColor(0xFFFFFFFF);
            holder.name.setTextColor(0xFFFFFFFF);
            holder.price.setTextColor(0xFFFFFFFF);
            holder.percentage.setTextColor(0xFFFFFFFF);
        }

        holder.symbol.setText(stock.getSymbol());

        holder.name.setText(stock.getCompany());

        holder.price.setText(String.format("%.02f",stock.getLatestPrice()));

        String comboChange = arrow + " " + (String.format("%.02f",stock.getChange()) + " (" + String.format("%.02f",stock.getChangePercentage()) + "%)");
        holder.percentage.setText(comboChange);

    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
