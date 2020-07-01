package com.golink.ecommerceb2bvendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.Products.SingleProduct;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.RecyclerViewHolder> {

    private Context mCtx;

    public FeaturedAdapter(Context mCtx, List<FeaturedItems> homeItemsList, String check) {
        this.mCtx = mCtx;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.featured_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
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
