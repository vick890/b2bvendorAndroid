package com.golink.ecommerceb2bvendor.Chat;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AllVendors extends Fragment {

    private List<FeaturedItems> homeItemsList = new ArrayList<>();
    FeaturedAdapter featuredAdapter;
    private RecyclerView categoryView;
    private String id, usertoken;
    private ProgressBar progressD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_all_vendors, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        categoryView = view.findViewById(R.id.vendorView);
        categoryView.setHasFixedSize(true);

        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VENDOR_REQUESTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        homeItemsList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("1")){
                                homeItemsList.add(new FeaturedItems(
                                        jsonObject2.getString("user_id"),
                                        jsonObject2.getString("user_name"),
                                        //jsonObject2.getString("address"),
                                        jsonObject2.getString("chat_id"),
                                        jsonObject2.getString("user_image_path")

                                ));
                            }




                        }
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }


                categoryView.setLayoutManager(new LinearLayoutManager(getActivity()));
                featuredAdapter = new FeaturedAdapter(getActivity(), homeItemsList, "chat");
                categoryView.setAdapter(featuredAdapter);

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



        return view;

    }
}
