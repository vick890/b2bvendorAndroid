package com.golink.ecommerceb2bvendor;

public class ProductItems {

    private String id;
    private String name;
    private String image;
    private String price;
    private String category;
    private String user_id;
    private String offer_price;
    private String offer_percentage;
    private String out_of_stock;

    public ProductItems(String id, String name, String image, String price, String category, String user_id, String offer_price, String offer_percentage, String out_of_stock) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.user_id = user_id;
        this.offer_price = offer_price;
        this.offer_percentage = offer_percentage;
        this.out_of_stock = out_of_stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public String getOffer_percentage() {
        return offer_percentage;
    }

    public String getOut_of_stock() {
        return out_of_stock;
    }
}
