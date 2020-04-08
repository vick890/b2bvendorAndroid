package com.golink.ecommerceb2bvendor.Chat;

public class FeaturedItems {

    private String id;
    private String name;
    private String location;
    private String image;


    public FeaturedItems(String id, String name, String location, String image) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }
}
