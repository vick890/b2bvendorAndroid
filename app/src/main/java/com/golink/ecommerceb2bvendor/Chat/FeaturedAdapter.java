package com.golink.ecommerceb2bvendor.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<FeaturedItems> homeItemsList;
    private String check;


    public FeaturedAdapter(Context mCtx, List<FeaturedItems> homeItemsList, String check) {
        this.mCtx = mCtx;
        this.homeItemsList = homeItemsList;
        this.check = check;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.featured_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final FeaturedItems homeItems = homeItemsList.get(position);


        holder.featureName.setText(homeItems.getName());
        //holder.featureLoc.setText(homeItems.getLocation());

        Picasso.get().load(Constants.IMAGE_URL + homeItems.getImage())
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.featureImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(Constants.IMAGE_URL + homeItems.getImage()).centerInside()
                        .fit().into(holder.featureImage);
            }

        });


        holder.linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, PersonalChat.class);
                intent.putExtra("uid", homeItems.getLocation());
                mCtx.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return homeItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureLoc;
        private LinearLayout linLay;
        private CircleImageView featureImage;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            featureName = itemView.findViewById(R.id.featureName);
            linLay = itemView.findViewById(R.id.linLay);
            featureImage = itemView.findViewById(R.id.featureImage);
            featureLoc = itemView.findViewById(R.id.featureCity);


        }
    }
}
