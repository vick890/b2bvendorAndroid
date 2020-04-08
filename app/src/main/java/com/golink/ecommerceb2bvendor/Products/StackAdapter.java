package com.golink.ecommerceb2bvendor.Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class StackAdapter extends PagerAdapter {
    private Context context;
    private List<String> feedItemsListUniversal;
    private String feedkey;



    public StackAdapter(List<String> feedItemsListUniversal, Context context, String feedkey) {
        this.feedItemsListUniversal = feedItemsListUniversal;
        this.context = context;
        this.feedkey = feedkey;
    }

    @Override
    public int getCount() {
        return feedItemsListUniversal.size();

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.image_items, null);


        final String image = feedItemsListUniversal.get(position);
        // Connecting with the frontend...

        final ImageView opImage = (ImageView) view.findViewById(R.id.imageCard);


        Picasso.get().load(Constants.IMAGE_URL + image)
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(opImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(Constants.IMAGE_URL + image).centerInside()
                        .fit().into(opImage);
            }

        });

        opImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context, CoverImages.class);
//                intent.putExtra("feedkey", feedkey);
//                context.startActivity(intent);

            }
        });




        container.addView(view);
        return view;


    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void displayToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

