package com.golink.ecommerceb2bvendor.Orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SingleOrder extends Fragment {

    private RecyclerView currentView, previousView;
    private List<SubscribeItems> subscribeItemsList = new ArrayList<>();
    private List<SubscribeItems> subscribeItemsListPrev = new ArrayList<>();
    SingleOrderAdapter subscribeAdapter;
    SingleOrderPreviousAdapter singleOrderPreviousAdapter;
    private String id, usertoken, orderid, userid;
    private ProgressBar progressD;
    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_single_order, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        Bundle b = this.getArguments();
        if(b!=null){
            orderid = (String) b.get("orderid");
            userid = (String) b.get("userid");
        }

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        final Button currentBtn = view.findViewById(R.id.currentBtn);
        final Button previousBtn = view.findViewById(R.id.previousBtn);
        final ImageButton calender = view.findViewById(R.id.calender);


        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        currentView = view.findViewById(R.id.currentView);
        currentView.setHasFixedSize(true);
        currentView.setLayoutManager(new LinearLayoutManager(getActivity()));

        previousView = view.findViewById(R.id.previousView);
        previousView.setHasFixedSize(true);
        previousView.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentBtn.setBackgroundResource(R.color.colorAccent);
                currentBtn.setTextColor(Color.parseColor("#FFFFFF"));
                previousBtn.setBackgroundResource(R.color.colorWhite);
                previousBtn.setTextColor(Color.parseColor("#1A7BA8"));
                currentView.setVisibility(View.VISIBLE);
                previousView.setVisibility(View.GONE);


            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                previousBtn.setBackgroundResource(R.color.colorAccent);
                previousBtn.setTextColor(Color.parseColor("#FFFFFF"));
                currentBtn.setBackgroundResource(R.color.colorWhite);
                currentBtn.setTextColor(Color.parseColor("#1A7BA8"));

                currentView.setVisibility(View.GONE);
                previousView.setVisibility(View.VISIBLE);

            }
        });


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ORDER_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                subscribeItemsList.clear();
                currentView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            subscribeItemsList.add(new SubscribeItems(
                                    null,
                                    jsonObject2.getString("name"),
                                    jsonObject2.getString("price"),
                                    jsonObject2.getString("quantity"),
                                    jsonObject2.getString("preview_image_path"),
                                    jsonObject2.getString("product_id"),
                                    jsonObject2.getString("order_color"),
                                    null,
                                    null
                            ));




                        }

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                //progressD.setVisibility(View.GONE);
                subscribeAdapter = new SingleOrderAdapter(getActivity(), subscribeItemsList);
                currentView.setAdapter(subscribeAdapter);



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
                paramMap.put("order_payment_id", orderid);

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);



        // previous


        final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.PREVIOUS_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                subscribeItemsListPrev.clear();
                previousView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("1")){
                                subscribeItemsListPrev.add(new SubscribeItems(
                                        jsonObject2.getString("order_payment_id"),
                                        null,
                                        jsonObject2.getString("total_amount"),
                                        null,
                                        null,
                                        null,
                                        jsonObject2.getString("indate"),
                                        null,
                                        null
                                ));
                            }

                        }

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                progressD.setVisibility(View.GONE);
                singleOrderPreviousAdapter = new SingleOrderPreviousAdapter(getActivity(), subscribeItemsListPrev, userid);
                previousView.setAdapter(singleOrderPreviousAdapter);



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
                paramMap.put("user_id", userid);

                return paramMap;

            }
        };

        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue2.add(stringRequest2);


        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                        String val = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calDate(val);
                    }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


//                long today = calendar.getTimeInMillis();
//                final long oneDay = 24 * 60 * 60 * 1000L;
//
//                Date prevDays = new Date(today - 1000);
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(prevDays);
//                datePickerDialog.setMinDate(cal);
//
//                Date nextDays = new Date(today + 14 * oneDay);
//                Calendar cal2 = Calendar.getInstance();
//                cal2.setTime(nextDays);
//                datePickerDialog.setMaxDate(cal2);
//
//
//                datePickerDialog.show(getFragmentManager(), "D");

            }
        });

        return view;
    }

    private void calDate (final String val) {


        progressD.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.PREVIOUS_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                subscribeItemsListPrev.clear();
                previousView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("1") && jsonObject2.getString("order_date").equals(val)){
                                subscribeItemsListPrev.add(new SubscribeItems(
                                        jsonObject2.getString("order_payment_id"),
                                        null,
                                        jsonObject2.getString("total_amount"),
                                        null,
                                        null,
                                        null,
                                        jsonObject2.getString("indate"),
                                        null,
                                        null
                                ));
                            }
                        }

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                progressD.setVisibility(View.GONE);
                singleOrderPreviousAdapter = new SingleOrderPreviousAdapter(getActivity(), subscribeItemsListPrev, userid);
                previousView.setAdapter(singleOrderPreviousAdapter);



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
                paramMap.put("user_id", userid);

                return paramMap;

            }
        };

        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue2.add(stringRequest2);

    }
}
