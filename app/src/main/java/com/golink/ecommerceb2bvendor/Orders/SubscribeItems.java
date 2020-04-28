package com.golink.ecommerceb2bvendor.Orders;

public class SubscribeItems {

    private String id;
    private String name;
    private String price;
    private String quantity;
    private String image;
    private String product_id;
    private String date;
    private String deliveries;
    private String days;


    public SubscribeItems(String id, String name, String price,
                          String quantity, String image, String product_id, String date, String deliveries, String days) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.product_id = product_id;
        this.date = date;
        this.deliveries = deliveries;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getDate() {
        return date;
    }

    public String getDeliveries() {
        return deliveries;
    }

    public String getDays() {
        return days;
    }
}
