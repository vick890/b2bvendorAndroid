package com.golink.ecommerceb2bvendor.Orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SingleOrderPreviousAdapter extends RecyclerView.Adapter<SingleOrderPreviousAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<SubscribeItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken, userid;

    public SingleOrderPreviousAdapter(Context mCtx, List<SubscribeItems> carItemsList, String userid) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
        this.userid = userid;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.previous_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final SubscribeItems carItems = carItemsList.get(position);

        mProgress = new ProgressDialog(mCtx);
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        holder.orderPrice.setText("₹ " + carItems.getPrice());
        holder.orderDate.setText("Order ID: " + carItems.getId());
        holder.orderStatus.setText(carItems.getDate());





        holder.cv_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment fragment = new SingleOrder();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", carItems.getId());
                bundle.putString("userid", userid);
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
        return carItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView orderPrice;
        private TextView orderStatus;
        private TextView orderDate;
        private CardView cv_personalInfo;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            cv_personalInfo = itemView.findViewById(R.id.cv_personalInfo);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);


        }
    }

}
