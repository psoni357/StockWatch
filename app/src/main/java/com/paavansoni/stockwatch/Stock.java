package com.paavansoni.stockwatch;

class Stock {
    private String symbol;
    private String company;
    private double latestPrice;
    private double change;
    private double changePercentage;

    Stock() {
        symbol = "Symbol";
        company = "Name";
        latestPrice = 0;
        change = 0;
        changePercentage = 0;
    }

    Stock(String symbol, String name, double latestPrice, double change, double changePercentage) {
        this.symbol = symbol;
        this.company = name;
        this.latestPrice = latestPrice;
        this.change = change;
        this.changePercentage = changePercentage;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
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

    public void setCompany(String name) {
        this.company = name;
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
