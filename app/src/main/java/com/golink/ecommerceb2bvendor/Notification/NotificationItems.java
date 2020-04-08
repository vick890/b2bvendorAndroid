package com.golink.ecommerceb2bvendor.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NotificationItems {

    private String type;
    private String connection;
    private String id;
    private String status;

    public NotificationItems(String type, String connection, String id, String status) {
        this.type = type;
        this.connection = connection;
        this.id = id;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public String getConnection() {
        return connection;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
