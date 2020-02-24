package com.paavansoni.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView symbol;
    TextView name;
    TextView price;
    TextView percentage;


    MyViewHolder(View itemView) {
        super(itemView);
        symbol = itemView.findViewById(R.id.company_symbol);
        name = itemView.findViewById(R.id.comapny_name);
        price = itemView.findViewById(R.id.company_price);
        percentage = itemView.findViewById(R.id.company_change);
    }
}
