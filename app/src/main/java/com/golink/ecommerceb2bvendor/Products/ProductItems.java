package com.golink.ecommerceb2bvendor.Products;

public class ProductItems {

    private String id;
    private String name;
    private String image;
    private String price;
    private String moq;
    private String color;

    public ProductItems(String id, String name, String image, String price, String moq, String color) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
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
}
