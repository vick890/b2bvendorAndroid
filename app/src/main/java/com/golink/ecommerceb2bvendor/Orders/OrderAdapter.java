package com.golink.ecommerceb2bvendor.Orders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.golink.ecommerceb2bvendor.MainActivity;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<OrderItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken;

    public OrderAdapter(Context mCtx, List<OrderItems> carItemsList) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final OrderItems carItems = carItemsList.get(position);

        mProgress = new ProgressDialog(mCtx);
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        holder.orderId.setText("Order ID: " + carItems.getId());
        holder.orderPrice.setText(carItems.getDate());
        holder.orderDate.setText(carItems.getName());
        holder.orderStatus.setText("₹ " + carItems.getAmount());




//        switch (carItems.getStatus()) {
//            case "1":
//                holder.progressOrder.setProgress(25);
//                break;
//            case "2":
//                holder.progressOrder.setProgress(50);
//                holder.cancelOrder.setVisibility(View.GONE);
//                break;
//            case "3":
//                holder.progressOrder.setProgress(75);
//                break;
//            case "4":
//                holder.progressOrder.setProgress(100);
//                break;
//        }



        holder.cv_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment fragment = new SingleOrder();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", carItems.getId());
                bundle.putString("userid", carItems.getUserid());
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mCtx, R.style.MyDialogTheme)
                        .setTitle("Order Delivered")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mProgress.setMessage("Please wait...");
                                mProgress.show();


                                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEND_DELIVERED, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {

                                        try {
                                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                            boolean error = jsonObject.getBoolean("error");
                                            String message = jsonObject.getString("message");

                                            Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();

                                            mProgress.dismiss();
                                            if(!error){

                                                Activity activity = (Activity)mCtx;
                                                Intent intent = new Intent(mCtx, MainActivity.class);
                                                intent.putExtra("page", "order");
                                                mCtx.startActivity(intent);
                                                activity.finish();

                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            mProgress.dismiss();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {


                                        mProgress.dismiss();
                                        Toast.makeText(mCtx, "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        Map<String, String> paramMap = new HashMap<String, String>();

                                        paramMap.put("userid", id);
                                        paramMap.put("usertoken", usertoken);
                                        paramMap.put("order_payment_id", carItems.getId());

                                        return paramMap;

                                    }
                                };

                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                requestQueue.add(stringRequest);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return carItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView orderId;
        private TextView orderPrice;
        private TextView orderStatus;
        private TextView orderDate;
        private CardView cv_personalInfo;
        private Button cancelOrder;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            cv_personalInfo = itemView.findViewById(R.id.cv_personalInfo);
            orderId = itemView.findViewById(R.id.orderId);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderDate = itemView.findViewById(R.id.orderDate);
            cancelOrder = itemView.findViewById(R.id.cancelOrder);


        }
    }

}
