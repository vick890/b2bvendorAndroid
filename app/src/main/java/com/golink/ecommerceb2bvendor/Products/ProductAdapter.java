package com.golink.ecommerceb2bvendor.Products;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<ProductItems> homeItemsList;


    public ProductAdapter(Context mCtx, List<ProductItems> homeItemsList) {
        this.mCtx = mCtx;
        this.homeItemsList = homeItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final ProductItems homeItems = homeItemsList.get(position);

        holder.prodName.setText(homeItems.getName());
        holder.prodPrice.setText("₹ " + homeItems.getPrice());
        holder.prodMoq.setText("MOQ (" + homeItems.getMoq() + ")");

        Picasso.get().load(Constants.IMAGE_URL + homeItems.getImage())
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.prodImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(Constants.IMAGE_URL + homeItems.getImage()).centerInside()
                        .fit().into(holder.prodImage);
            }

        });



        if(!homeItems.getColor().equals("null")) {
            final List<String> colorList = Arrays.asList(homeItems.getColor().split(","));

            if (colorList.contains("black")) {
                holder.blackIm.setVisibility(View.VISIBLE);
            }
            if (colorList.contains("white")) {
                holder.whiteIm.setVisibility(View.VISIBLE);
            }
            if (colorList.contains("red")) {
                holder.redIm.setVisibility(View.VISIBLE);
            }
            if (colorList.contains("green")) {
                holder.greenIm.setVisibility(View.VISIBLE);
            }
            if (colorList.contains("blue")) {
                holder.blueIm.setVisibility(View.VISIBLE);
            }

        }else{
            holder.fake.setVisibility(View.VISIBLE);
        }


            holder.linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("feedkey", homeItems.getId());
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return homeItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView prodName;
        private TextView prodPrice;
        private TextView prodMoq;
        private CardView linLay;
        private ImageView prodImage;
        private CircleImageView blackIm, whiteIm, redIm, greenIm, blueIm, fake;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            prodName = itemView.findViewById(R.id.prodName);
            linLay = itemView.findViewById(R.id.linLay);
            prodImage = itemView.findViewById(R.id.prodImage);
            prodPrice = itemView.findViewById(R.id.prodPrice);
            prodMoq = itemView.findViewById(R.id.prodMoq);
            blackIm = itemView.findViewById(R.id.blackIm);
            whiteIm = itemView.findViewById(R.id.whiteIm);
            redIm = itemView.findViewById(R.id.redIm);
            greenIm = itemView.findViewById(R.id.greenIm);
            blueIm = itemView.findViewById(R.id.blueIm);
            fake = itemView.findViewById(R.id.fake);



        }
    }
}
