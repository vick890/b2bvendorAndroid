package com.golink.ecommerceb2bvendor.Requests;

public class RequestItems {

    private String id;
    private String name;
    private String image;
    private String address;
    private String mobile;
    private String userid;
    private String status;

    public RequestItems(String id, String name, String image, String address, String mobile, String userid, String status) {

        this.id = id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.mobile = mobile;
        this.userid = userid;
        this.status = status;
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

    public String getUserid() {
        return userid;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }
}
