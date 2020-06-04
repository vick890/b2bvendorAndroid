package com.golink.ecommerceb2bvendor.Products;

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
    private String moq;
    private String color;

    public ProductItems(String id, String name, String image, String price, String category, String user_id, String offer_price, String offer_percentage, String out_of_stock, String moq, String color) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.category = category;
        this.user_id = user_id;
        this.offer_price = offer_price;
        this.offer_percentage = offer_percentage;
        this.out_of_stock = out_of_stock;
        this.moq = moq;
        this.color = color;
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

    public String getMoq() {
        return moq;
    }

    public String getColor() {
        return color;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getOffer_percentage() {
        return offer_percentage;
    }

    public void setOffer_percentage(String offer_percentage) {
        this.offer_percentage = offer_percentage;
    }

    public String getOut_of_stock() {
        return out_of_stock;
    }

    public void setOut_of_stock(String out_of_stock) {
        this.out_of_stock = out_of_stock;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
