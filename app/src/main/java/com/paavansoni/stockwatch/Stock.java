package com.paavansoni.stockwatch;

class Stock {
    private String symbol;
    private String name;
    private double latestPrice;
    private double change;
    private double changePercentage;

    Stock() {
        symbol = "Symbol";
        name = "Name";
        latestPrice = 0;
        change = 0;
        changePercentage = 0;
    }

    Stock(String symbol, String name, double latestPrice, double change, double changePercentage) {
        this.symbol = symbol;
        this.name = name;
        this.latestPrice = latestPrice;
        this.change = change;
        this.changePercentage = changePercentage;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public double getChange() {
        return change;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercentage(double changePercentage) {
        this.changePercentage = changePercentage;
    }
}
