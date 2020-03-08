package com.paavansoni.stockwatch;



class Stock implements Comparable{
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

    String getSymbol() {
        return symbol;
    }

    String getCompany() {
        return company;
    }

    double getLatestPrice() {
        return latestPrice;
    }

    double getChange() {
        return change;
    }


    double getChangePercentage() {
        return changePercentage;
    }
    /*
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
    */
    @Override
    public int compareTo(Object o) {
        Stock toComp = (Stock)o;
        return (this.getSymbol().compareTo(toComp.getSymbol()));
    }
}
