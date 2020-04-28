package com.golink.ecommerceb2bvendor.Requests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.golink.ecommerceb2bvendor.Dashboard;
import com.golink.ecommerceb2bvendor.MainActivity;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Registration.OtpPage;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<RequestItems> carItemsList;
    private ProgressDialog mProgress;
    private String id, usertoken, check;

    public RequestAdapter(Context mCtx, List<RequestItems> carItemsList, String check) {
        this.mCtx = mCtx;
        this.carItemsList = carItemsList;
        this.check = check;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.request_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final RequestItems carItems = carItemsList.get(position);

        mProgress = new ProgressDialog(mCtx);
        SharedPreferences sharedPreferences2 = mCtx.getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        if(check.equals("request")){
            holder.linLay2.setVisibility(View.VISIBLE);
            holder.linear1.setVisibility(View.GONE);

        }else {
            holder.linear1.setVisibility(View.VISIBLE);
            holder.linLay2.setVisibility(View.GONE);
        }

        holder.featureName.setText(carItems.getName());
        holder.featureAddress.setText(carItems.getAddress());
        holder.featureMobile.setText(carItems.getMobile());

        switch (carItems.getStatus()) {
            case "0":
                holder.featureStatus.setText("Pending...");
                break;
            case "1":
                holder.featureStatus.setText("JOINED");
                break;
            case "2":
                holder.featureStatus.setText("REJECTED");
                break;
        }


        Picasso.get().load(Constants.IMAGE_URL + carItems.getImage())
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.featureImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {

                Picasso.get().load(Constants.IMAGE_URL + carItems.getImage()).centerInside()
                        .fit().into(holder.featureImage);
            }

        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Removing User...");
                mProgress.show();
                final String requestId = carItems.getId();
                final RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DELETE_USER_REQUEST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean error = jsonObject.getBoolean("error");

                            if(!error){

                                Toast.makeText(mCtx, "User deleted successfully!", Toast.LENGTH_LONG).show();

                                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REJECT_REQUEST, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {

                                        try {
                                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                            boolean error = jsonObject.getBoolean("error");

                                            if(!error){

                                                mProgress.dismiss();
                                                Toast.makeText(mCtx, "Done.", Toast.LENGTH_SHORT).show();

                                                Activity activity = (Activity)mCtx;
                                                Intent intent = new Intent(mCtx, MainActivity.class);
                                                intent.putExtra("page", "main");
                                                mCtx.startActivity(intent);
                                                activity.finish();


                                            } else {

                                                String message = jsonObject.getString("message");

                                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                                mProgress.dismiss();

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
                                        paramMap.put("request_id", requestId);

                                        return paramMap;

                                    }
                                };

                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                requestQueue.add(stringRequest);


                            }
                            else
                                {

                                String message = jsonObject.getString("message");

                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> paramMap = new HashMap<String, String>();

                        paramMap.put("userid", id);
                        paramMap.put("usertoken", usertoken);
                        paramMap.put("request_id",requestId);

                        return paramMap;

                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(stringRequest);

            }
        });



        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setMessage("Accepting...");
                mProgress.show();


                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ACCEPT_REQUEST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        try {
                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean error = jsonObject.getBoolean("error");

                            if(!error){

                                mProgress.dismiss();
                                Toast.makeText(mCtx, "Request accepted successfully!", Toast.LENGTH_SHORT).show();

                                Activity activity = (Activity)mCtx;
                                Intent intent = new Intent(mCtx, MainActivity.class);
                                intent.putExtra("page", "request");
                                mCtx.startActivity(intent);
                                activity.finish();


                            } else {

                                String message = jsonObject.getString("message");

                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();

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
                        paramMap.put("request_id", carItems.getId());


                        return paramMap;

                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(stringRequest);



            }
        });



        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setMessage("Rejecting...");
                mProgress.show();


                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REJECT_REQUEST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {

                        try {
                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean error = jsonObject.getBoolean("error");

                            if(!error){

                                mProgress.dismiss();
                                Toast.makeText(mCtx, "Request rejected successfully!", Toast.LENGTH_SHORT).show();

                                Activity activity = (Activity)mCtx;
                                Intent intent = new Intent(mCtx, MainActivity.class);
                                intent.putExtra("page", "request");
                                mCtx.startActivity(intent);
                                activity.finish();


                            } else {

                                String message = jsonObject.getString("message");

                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();

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
                        paramMap.put("request_id", carItems.getId());

                        return paramMap;

                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carItemsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureAddress;
        private TextView featureMobile;
        private CircleImageView featureImage;
        private Button acceptBtn;
        private Button rejectBtn,deleteBtn;
        private TextView featureStatus;
        private LinearLayout linLay2,linLay,linear1;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            featureName = itemView.findViewById(R.id.featureName);
            featureAddress = itemView.findViewById(R.id.featureAddress);
            featureMobile = itemView.findViewById(R.id.featureMobile);
            featureImage = itemView.findViewById(R.id.featureImage);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            featureStatus = itemView.findViewById(R.id.featureStatus);
            linLay2 = itemView.findViewById(R.id.linLay2);
            linLay = itemView.findViewById(R.id.linLay);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }

}
