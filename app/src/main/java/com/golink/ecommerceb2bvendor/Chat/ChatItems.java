package com.golink.ecommerceb2bvendor.Chat;

public class ChatItems {
    private String id;
    private String name;
    private String message;
    private String date;
    private String time;

    public ChatItems() {
    }


    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
