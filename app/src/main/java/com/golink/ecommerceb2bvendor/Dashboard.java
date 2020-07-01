package com.golink.ecommerceb2bvendor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.golink.ecommerceb2bvendor.Analysis.AnalysisPage;
import com.golink.ecommerceb2bvendor.Chat.ChatBoard;
import com.golink.ecommerceb2bvendor.Invite.InvitePage;
import com.golink.ecommerceb2bvendor.Orders.OrderPage;
import com.golink.ecommerceb2bvendor.Products.ProductsPage;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Requests.RequestItems;
import com.golink.ecommerceb2bvendor.Requests.RequestPage;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.golink.ecommerceb2bvendor.Service.GoLink.CHANNEL_1_ID;

public class Dashboard extends Fragment {

    private List<RequestItems> orderItemsList = new ArrayList<>();
    private String id, usertoken;
    private ProgressBar progressD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_dashboard, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");


        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        RelativeLayout orderBtn = view.findViewById(R.id.orderBtn);
        RelativeLayout chatBtn = view.findViewById(R.id.chatBtn);
        final RelativeLayout productBtn = view.findViewById(R.id.productBtn);
        RelativeLayout inviteBtn = view.findViewById(R.id.inviteBtn);
        RelativeLayout analysisBtn = view.findViewById(R.id.analysisBtn);
        RelativeLayout requestBtn = view.findViewById(R.id.requestBtn);
        final TextView reqNumber = view.findViewById(R.id.reqNumber);
        final TextView orderNumber = view.findViewById(R.id.orderNumber);

        final RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.COUNT_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONObject obj1 = jsonObject.getJSONObject("data");
                        String count = obj1.getString("count");


                        if(count.equals("0")){
                            orderNumber.setVisibility(View.GONE);
                        }else {
                            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,
                                    new Intent(getActivity(), LogIn.class), PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());

                            Notification notification = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID)
                                    .setContentTitle("New orders")
                                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setSmallIcon(R.drawable.logo_icon)
                                    .setColor(getResources().getColor(R.color.transparent))
                                    .setContentIntent(contentIntent)
                                    .build();

                            notificationManagerCompat.notify(1, notification);
                            orderNumber.setVisibility(View.VISIBLE);
                           // orderNumber.setText(String.valueOf(orderItemsList.size()));
                            orderNumber.setText(count);
                        }


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                //progressD.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //progressD.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramMap = new HashMap<String, String>();

                paramMap.put("userid", id);
                paramMap.put("usertoken", usertoken);

                return paramMap;

            }
        };

        stringRequest3.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue3.add(stringRequest3);





        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VENDOR_REQUESTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                orderItemsList.clear();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("0")){
                                orderItemsList.add(new RequestItems(
                                        jsonObject2.getString("id"),
                                        jsonObject2.getString("user_name"),
                                        jsonObject2.getString("user_image_path"),
                                        jsonObject2.getString("user_address"),
                                        jsonObject2.getString("user_mobile"),
                                        jsonObject2.getString("user_id"),
                                        jsonObject2.getString("status")
                                ));
                            }




                        }


                        if(orderItemsList.size() == 0){
                            reqNumber.setVisibility(View.GONE);
                        }else {
                            reqNumber.setVisibility(View.VISIBLE);
                            reqNumber.setText(String.valueOf(orderItemsList.size()));
                        }


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }


                progressD.setVisibility(View.GONE);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressD.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramMap = new HashMap<String, String>();

                paramMap.put("userid", id);
                paramMap.put("usertoken", usertoken);

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Fragment fragment = new ProductsPage();
                Fragment fragment = new VendorPage();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);

                Fragment fragment = new OrderPage();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RequestPage();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        analysisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AnalysisPage();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new InvitePage();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ChatBoard();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;

    }
}
