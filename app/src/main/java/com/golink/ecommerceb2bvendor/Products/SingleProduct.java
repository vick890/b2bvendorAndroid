package com.golink.ecommerceb2bvendor.Products;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class SingleProduct extends Fragment {

    private CircleImageView blackIm, whiteIm, redIm, greenIm, blueIm;
    private TextView nameProduct, priceProduct, colorName;
    private NumberPicker numberPicker;
    private List<CategoryItems> homeItemsList = new ArrayList<>();
    private String id, usertoken, feedkey;
    private ProgressBar progressD;

    private ViewPager viewPager;

    StackAdapter mainAdapter;
    List<String> feedItemsListUniversal = new ArrayList<>();

    private ProgressDialog mPorgress;

    private String checkColor;
    private TextView addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_single_product, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");


        Bundle b = this.getArguments();
        if(b!=null){

            feedkey = b.getString("feedkey");
            checkColor = b.getString("checkColor");
        }

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        mPorgress = new ProgressDialog(getActivity());

        nameProduct = view.findViewById(R.id.nameProduct);
        priceProduct = view.findViewById(R.id.priceProduct);
        addBtn = view.findViewById(R.id.addBtn);
        colorName = view.findViewById(R.id.colorName);

        blackIm = view.findViewById(R.id.blackIm);
        whiteIm = view.findViewById(R.id.whiteIm);
        redIm = view.findViewById(R.id.redIm);
        greenIm = view.findViewById(R.id.greenIm);
        blueIm = view.findViewById(R.id.blueIm);


        viewPager = (ViewPager) view.findViewById(R.id.gymImage);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabDots);


        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);


        ImageButton delIcon = view.findViewById(R.id.delIcon);
        ImageButton editIcon = view.findViewById(R.id.editIcon);



        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PRODUCT_DETAIl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONObject obj1 = jsonObject.getJSONObject("data");
                        final String name = obj1.getString("name");
                        final String price = obj1.getString("price");
                        final String moq = obj1.getString("moq");
                        final String color = obj1.getString("color");

                        nameProduct.setText(name);
                        priceProduct.setText("₹ " + price);
                        addBtn.setText("MOQ (" + moq + ")");

                        if(!color.equals("null")){
                            final List<String> colorList = Arrays.asList(color.split(","));

                            if(colorList.contains("black")){
                                blackIm.setVisibility(View.VISIBLE);
                            }
                            if(colorList.contains("white")){
                                whiteIm.setVisibility(View.VISIBLE);
                            }
                            if(colorList.contains("red")){
                                redIm.setVisibility(View.VISIBLE);
                            }
                            if(colorList.contains("green")){
                                greenIm.setVisibility(View.VISIBLE);
                            }
                            if(colorList.contains("blue")){
                                blueIm.setVisibility(View.VISIBLE);
                            }

                            if(checkColor==null){
                                checkColor = colorList.get(0);
                                colorName.setText("Selected Color : " + colorList.get(0));
                            }else{
                                colorName.setText("Selected Color : " + checkColor);
                            }

                            if(getActivity()!=null){


                                feedItemsListUniversal.clear();

                                final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.PRODUCT_IMAGES, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {

                                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                            boolean error = jsonObject.getBoolean("error");

                                            if(!error){


                                                JSONArray jsonArrayCat = jsonObject.getJSONArray("data");
                                                for (int i = 0; i < jsonArrayCat.length(); i++) {
                                                    final JSONObject jsonObjectCat = jsonArrayCat.getJSONObject(i);

                                                    feedItemsListUniversal.add(jsonObjectCat.getString("image"));

                                                }




                                            }


                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }


                                        mainAdapter = new StackAdapter(feedItemsListUniversal, getActivity(), feedkey);
                                        viewPager.setAdapter(mainAdapter);
                                        tabLayout.setupWithViewPager(viewPager, true);


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
                                        paramMap.put("product_id", feedkey);
                                        paramMap.put("color", checkColor);

                                        return paramMap;

                                    }
                                };

                                stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                requestQueue2.add(stringRequest2);

                            }



                        }else{

                            final String preview = obj1.getString("preview_image_path");

                            feedItemsListUniversal.clear();
                            feedItemsListUniversal.add(preview);
                            mainAdapter = new StackAdapter(feedItemsListUniversal, getActivity(), feedkey);
                            viewPager.setAdapter(mainAdapter);
                            tabLayout.setupWithViewPager(viewPager, true);
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
                paramMap.put("product_id", feedkey);

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);



        // Color Clicks


        blackIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkColor", "black");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        whiteIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkColor", "white");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        redIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkColor", "red");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        greenIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkColor", "green");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        blueIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new SingleProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkColor", "blue");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AddProduct();
                Bundle bundle = new Bundle();
                bundle.putString("checkPage", "edit");
                bundle.putString("feedkey", feedkey);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });





        delIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getActivity()!=null){

                    mPorgress.setMessage("Deleting...");
                    mPorgress.show();


                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DELETE_PRODUCT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            try {
                                final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                boolean error = jsonObject.getBoolean("error");

                                if(!error){

                                    mPorgress.dismiss();

                                    Toast.makeText(getActivity(), "Product deleted successfully!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("page", "product");
                                    startActivity(intent);
                                    getActivity().finish();



                                } else {

                                    String message = jsonObject.getString("message");

                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    mPorgress.dismiss();

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                mPorgress.dismiss();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            mPorgress.dismiss();
                            Toast.makeText(getActivity(), "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> paramMap = new HashMap<String, String>();

                            paramMap.put("product_id", feedkey);
                            paramMap.put("userid", id);
                            paramMap.put("usertoken", usertoken);

                            return paramMap;

                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(stringRequest);

                }


            }
        });









        return view;
    }



}

