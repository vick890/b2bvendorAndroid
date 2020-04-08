package com.golink.ecommerceb2bvendor.Orders;

public class OrderItems {

    private String id;
    private String name;
    private String date;
    private String amount;
    private String userid;

    public OrderItems(String id, String name, String date, String amount, String userid) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getUserid() {
        return userid;
    }
}
