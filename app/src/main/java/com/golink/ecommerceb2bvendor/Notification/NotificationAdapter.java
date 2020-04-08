package com.golink.ecommerceb2bvendor.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.Orders.OrderPage;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Requests.RequestPage;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<NotificationItems> homeItemsList;


    public NotificationAdapter(Context mCtx, List<NotificationItems> homeItemsList) {
        this.mCtx = mCtx;
        this.homeItemsList = homeItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final NotificationItems homeItems = homeItemsList.get(position);

        if(homeItems.getType().equals("1")){
           holder.textNot.setText("You have 1 new Order with Order ID: " + homeItems.getConnection() + "!");
        }else if(homeItems.getType().equals("2")){
            holder.textNot.setText("You have 1 new Request! Tap to View.");
        }



        holder.textNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(homeItems.getType().equals("1")){

                    Fragment fragment = new OrderPage();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame_second, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else if(homeItems.getType().equals("2")){

                    Fragment fragment = new RequestPage();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame_second, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return homeItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView textNot;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            textNot = itemView.findViewById(R.id.textNot);



        }
    }
}
