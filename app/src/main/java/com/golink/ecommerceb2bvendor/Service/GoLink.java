package com.golink.ecommerceb2bvendor.Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class GoLink extends Application{

        public static final String CHANNEL_1_ID = "channel1";

        @Override
        public void onCreate() {
            super.onCreate();

            createNotification();

        }

        private void createNotification() {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_1_ID,
                        "channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );

                channel1.setDescription("Hurry! 1 New Order!");

                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel1);
            }
        }
}

