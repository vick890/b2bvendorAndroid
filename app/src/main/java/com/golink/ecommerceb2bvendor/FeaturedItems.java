package com.golink.ecommerceb2bvendor;

import java.io.Serializable;

public class FeaturedItems implements Serializable {

    private String id;
    private String name;
    private String location;
    private String image;
    private String user_id;
    private String category_id;


    public FeaturedItems(String id, String name, String location, String image, String user_id, String category_id) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.image = image;
        this.user_id = user_id;
        this.category_id = category_id;
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

    public String getUser_id() {
        return user_id;
    }

    public String getCategory_id() {
        return category_id;
    }
}
