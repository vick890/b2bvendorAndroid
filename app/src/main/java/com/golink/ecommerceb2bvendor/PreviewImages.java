package com.golink.ecommerceb2bvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PreviewImages extends AppCompatActivity {

    private ImageView previewImages;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_images);

        Intent intent = getIntent();
        bitmaps = intent.getParcelableArrayListExtra("BitmapImages");

        previewImages = findViewById(R.id.previewImages);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (final Bitmap b: bitmaps){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previewImages.setImageBitmap(b);
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
