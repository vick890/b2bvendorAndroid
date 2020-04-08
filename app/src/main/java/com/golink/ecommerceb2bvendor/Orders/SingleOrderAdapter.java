package com.golink.ecommerceb2bvendor.Orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SingleOrderAdapter extends RecyclerView.Adapter<SingleOrderAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<SubscribeItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken;


    public SingleOrderAdapter(Context mCtx, List<SubscribeItems> carItemsList) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.subscribe_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final SubscribeItems carItems = carItemsList.get(position);

        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        mProgress = new ProgressDialog(mCtx);

        holder.cartName.setText(("Model: " + carItems.getName()));
        holder.cartColor.setText(("Color: " + carItems.getDate()));

        int val = Integer.valueOf(carItems.getPrice());
        int qtyval = Integer.valueOf(carItems.getQuantity());
        holder.cartPrice2.setText(("Price: ₹ " + (val / qtyval)));

        holder.cartPrice.setText(("₹ " + val));
        holder.cartQuantity.setText(("Qty: " + carItems.getQuantity()));

        Picasso.get().load((Constants.IMAGE_URL + carItems.getImage()))
                .centerInside().resize(500, 500)
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.cartImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load((Constants.IMAGE_URL + carItems.getImage())).centerInside()
                        .resize(500, 500).into(holder.cartImage);

            }
        });



    }

    @Override
    public int getItemCount() {
        return carItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView cartName;
        private TextView cartPrice;
        private ImageView cartImage;
        private TextView cartQuantity;
        private TextView cartPrice2;
        private TextView cartColor;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            cartName = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartImage = itemView.findViewById(R.id.cartImage);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            cartPrice2 = itemView.findViewById(R.id.cartPrice2);
            cartColor = itemView.findViewById(R.id.cartColor);


        }
    }

}
