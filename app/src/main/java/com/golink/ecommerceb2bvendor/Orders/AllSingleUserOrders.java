package com.golink.ecommerceb2bvendor.Orders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AllSingleUserOrders extends Fragment {

    private RecyclerView currentView, previousView;
    private List<SubscribeItems> subscribeItemsList = new ArrayList<>();
    private List<SubscribeItems> subscribeItemsListPrev = new ArrayList<>();
    SingleOrderAdapter subscribeAdapter;
    SingleUserAllOrdersAdapter singleOrderPreviousAdapter;
    private String id, usertoken, orderid, userid;
    private ProgressBar progressD;
    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.all_single_user_orders, container, false);
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

        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        previousView = view.findViewById(R.id.previousView);
        previousView.setHasFixedSize(true);
        previousView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                                        jsonObject2.getString("business_name"),
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
                singleOrderPreviousAdapter = new SingleUserAllOrdersAdapter(getActivity(), subscribeItemsListPrev, userid);
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



        return view;
    }
}
