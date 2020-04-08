package com.golink.ecommerceb2bvendor.Analysis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SellingAdapter extends RecyclerView.Adapter<SellingAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<SellingItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken;

    public SellingAdapter(Context mCtx, List<SellingItems> carItemsList) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.selling_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final SellingItems carItems = carItemsList.get(position);

        mProgress = new ProgressDialog(mCtx);
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        holder.orderPrice.setText(carItems.getQuantity());
        holder.orderDate.setText(carItems.getName());
        holder.orderStatus.setText("₹ " + carItems.getPrice());





    }

    @Override
    public int getItemCount() {
        return carItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView orderPrice;
        private TextView orderStatus;
        private TextView orderDate;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);


        }
    }

}
