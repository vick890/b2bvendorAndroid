package com.golink.ecommerceb2bvendor.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.golink.ecommerceb2bvendor.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView logo = findViewById(R.id.logo);

        Picasso.get().load((R.drawable.logo_icon))
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(logo, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load((R.drawable.logo_icon)).centerInside()
                        .fit().into(logo);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent feedIntent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(feedIntent);
                finish();
            }
        }, 2000);

    }
}
