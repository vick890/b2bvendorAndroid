package com.golink.ecommerceb2bvendor.Orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class OrderPage extends Fragment {

    private RecyclerView currentView;
    private String id, usertoken;
    private List<OrderItems> orderItemsList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private ProgressBar progressD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_order_page, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");


        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        currentView = view.findViewById(R.id.currentView);
        currentView.setHasFixedSize(true);
        currentView.setLayoutManager(new LinearLayoutManager(getActivity()));


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                orderItemsList.clear();
                currentView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                          //  if(jsonObject2.getString("user_name").equals("Rajat")){
                           //     for (int j=0;i<jsonObject2.length();j++){
                                    orderItemsList.add(new OrderItems(
                                            jsonObject2.getString("order_payment_id"),
                                            jsonObject2.getString("user_name"),
                                            jsonObject2.getString("indate"),
                                            jsonObject2.getString("total_amount"),
                                            jsonObject2.getString("user_id")
                                    ));
                             //   }

                         //   }
                        }
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                progressD.setVisibility(View.GONE);
                orderAdapter = new OrderAdapter(getActivity(), orderItemsList);
                currentView.setAdapter(orderAdapter);
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

        /*final RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.UPDATE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

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
        */

        final RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.UPDATE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));



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

        return view;
    }
}
