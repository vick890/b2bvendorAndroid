package com.golink.ecommerceb2bvendor.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProductsPage extends Fragment {

    private RecyclerView currentView;
    private String id, usertoken;
    private List<ProductItems> orderItemsList = new ArrayList<>();
    private ProductAdapter orderAdapter;
    private ProgressBar progressD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_products_page, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");


        CircleImageView addBtn = view.findViewById(R.id.addBtn);

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), usertoken, Toast.LENGTH_LONG).show();

        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        currentView = view.findViewById(R.id.productView);
        currentView.setHasFixedSize(true);
        currentView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PRODUCTS, new Response.Listener<String>() {
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


                            if(jsonObject2.getString("user_id").equals(id)){
                                orderItemsList.add(new ProductItems(
                                        jsonObject2.getString("product_id"),
                                        jsonObject2.getString("name"),
                                        jsonObject2.getString("preview_image_path"),
                                        jsonObject2.getString("price"),
                                        jsonObject2.getString("moq"),
                                        jsonObject2.getString("color")
                                ));
                            }

                        }


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                progressD.setVisibility(View.GONE);
                orderAdapter = new ProductAdapter(getActivity(), orderItemsList);
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







        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AddProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkPage", "null");
                fragment.setArguments(bundle);
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
