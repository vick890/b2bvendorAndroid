package com.golink.ecommerceb2bvendor.Analysis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.golink.ecommerceb2bvendor.Profile.ProfilePage;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<SellingItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken;

    public BuyerAdapter(Context mCtx, List<SellingItems> carItemsList) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.buyer_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final SellingItems carItems = carItemsList.get(position);

        mProgress = new ProgressDialog(mCtx);
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        holder.featureName.setText(carItems.getName());
        holder.featureCity.setText("₹ " + carItems.getPrice());


        Picasso.get().load(Constants.IMAGE_URL + carItems.getQuantity())
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.featureImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(Constants.IMAGE_URL + carItems.getQuantity()).centerInside()
                        .fit().into(holder.featureImage);
            }

        });


        holder.linLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProfilePage();
                Bundle bundle = new Bundle();
                bundle.putString("user", carItems.getId());
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
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

        private TextView featureName;
        private TextView featureCity;
        private CircleImageView featureImage;
        private LinearLayout linLay;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            featureName = itemView.findViewById(R.id.featureName);
            featureCity = itemView.findViewById(R.id.featureCity);
            featureImage = itemView.findViewById(R.id.featureImage);
            linLay = itemView.findViewById(R.id.linLay);


        }
    }

}
