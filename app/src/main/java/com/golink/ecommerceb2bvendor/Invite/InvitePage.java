package com.golink.ecommerceb2bvendor.Invite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Requests.RequestAdapter;
import com.golink.ecommerceb2bvendor.Requests.RequestItems;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.golink.ecommerceb2bvendor.Utils.Validations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class InvitePage extends Fragment {

    private RecyclerView allView, joinedView, pendingView;
    private List<RequestItems> reqAll = new ArrayList<>();
    private List<RequestItems> reqPending = new ArrayList<>();
    private List<RequestItems> reqJoined = new ArrayList<>();
    private RequestAdapter requestAdapterAll, requestAdapterPending, requestAdapterJoined;
    private String id, usertoken, name, refer;
    private ProgressBar progressD;
    private ProgressDialog mProgress;
    private EditText productSearchEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_invite_page, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");
        name = sharedPreferences2.getString("name", "");
        refer = sharedPreferences2.getString("refer", "");

        final Button allBtn = view.findViewById(R.id.allBtn);
        final Button pendingBtn = view.findViewById(R.id.pendingBtn);
        final Button joinedBtn = view.findViewById(R.id.joinedBtn);

        mProgress = new ProgressDialog(getActivity());

        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        allView = view.findViewById(R.id.allView);
        allView.setHasFixedSize(true);
        allView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pendingView = view.findViewById(R.id.pendingView);
        pendingView.setHasFixedSize(true);
        pendingView.setLayoutManager(new LinearLayoutManager(getActivity()));

        joinedView = view.findViewById(R.id.joinedView);
        joinedView.setHasFixedSize(true);
        joinedView.setLayoutManager(new LinearLayoutManager(getActivity()));


        TextView vendorSearch = view.findViewById(R.id.vendorSearch);
        final TextView productSearch = view.findViewById(R.id.productSearch);
        productSearchEdit = view.findViewById(R.id.productSearchEdit);
        final Button sendBtn = view.findViewById(R.id.sendBtn);

        vendorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSubject = "GOLINK";
                String shareBody = "Download the app GOLINK now! Invited by: " + name + "\nUse my refer code: " + refer;
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share Using"));


            }
        });


        productSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productSearch.setVisibility(View.GONE);
                productSearchEdit.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);

                productSearchEdit.post(new Runnable() {
                    @Override
                    public void run() {
                        productSearchEdit.requestFocus();
                        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imgr.showSoftInput(productSearchEdit, InputMethodManager.SHOW_IMPLICIT);
                    }
                });


            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String num = productSearchEdit.getText().toString();

                if(!TextUtils.isEmpty(num) && Validations.isValidMobile(num, 10)){

                    mProgress.setMessage("Please Wait...");
                    mProgress.show();


                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SEND_INVITE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            try {
                                final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                boolean error = jsonObject.getBoolean("error");

                                if(!error){

                                    Toast.makeText(getActivity(), "Invite sent successfully!", Toast.LENGTH_SHORT).show();

                                    productSearchEdit.setText("");



                                } else {

                                    String message = jsonObject.getString("message");

                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mProgress.dismiss();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            mProgress.dismiss();
                            //Toast.makeText(getActivity(), "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> paramMap = new HashMap<String, String>();

                            paramMap.put("userid", id);
                            paramMap.put("usertoken", usertoken);
                            paramMap.put("mobile", num);

                            return paramMap;

                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(stringRequest);

                }else {
                    Toast.makeText(getActivity(), "Please enter 10 digit mobile number!", Toast.LENGTH_SHORT).show();
                }

            }
        });






        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allBtn.setBackgroundResource(R.color.colorAccent);
                allBtn.setTextColor(Color.parseColor("#FFFFFF"));
                pendingBtn.setBackgroundResource(R.color.colorWhite);
                pendingBtn.setTextColor(Color.parseColor("#1A7BA8"));
                joinedBtn.setBackgroundResource(R.color.colorWhite);
                joinedBtn.setTextColor(Color.parseColor("#1A7BA8"));
                allView.setVisibility(View.VISIBLE);
                pendingView.setVisibility(View.GONE);
                joinedView.setVisibility(View.GONE);


            }
        });

        pendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pendingBtn.setBackgroundResource(R.color.colorAccent);
                pendingBtn.setTextColor(Color.parseColor("#FFFFFF"));
                allBtn.setBackgroundResource(R.color.colorWhite);
                allBtn.setTextColor(Color.parseColor("#1A7BA8"));
                joinedBtn.setBackgroundResource(R.color.colorWhite);
                joinedBtn.setTextColor(Color.parseColor("#1A7BA8"));

                allView.setVisibility(View.GONE);
                pendingView.setVisibility(View.VISIBLE);
                joinedView.setVisibility(View.GONE);

            }
        });

        joinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joinedBtn.setBackgroundResource(R.color.colorAccent);
                joinedBtn.setTextColor(Color.parseColor("#FFFFFF"));
                allBtn.setBackgroundResource(R.color.colorWhite);
                allBtn.setTextColor(Color.parseColor("#1A7BA8"));
                pendingBtn.setBackgroundResource(R.color.colorWhite);
                pendingBtn.setTextColor(Color.parseColor("#1A7BA8"));

                allView.setVisibility(View.GONE);
                joinedView.setVisibility(View.VISIBLE);
                pendingView.setVisibility(View.GONE);

            }
        });


        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.VENDOR_REQUESTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                reqAll.clear();
                allView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            reqAll.add(new RequestItems(
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


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                requestAdapterAll = new RequestAdapter(getActivity(), reqAll, "view");
                allView.setAdapter(requestAdapterAll);



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

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


        // pending


        final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.VENDOR_REQUESTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                reqPending.clear();
                pendingView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("0")){
                                reqPending.add(new RequestItems(
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


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                requestAdapterPending = new RequestAdapter(getActivity(), reqPending, "view");
                pendingView.setAdapter(requestAdapterPending);



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

                return paramMap;

            }
        };

        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue2.add(stringRequest2);



        // joined

        final RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.VENDOR_REQUESTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                reqJoined.clear();
                joinedView.removeAllViews();

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if(jsonObject2.getString("status").equals("1")){
                                reqJoined.add(new RequestItems(
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


                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                progressD.setVisibility(View.GONE);
                requestAdapterJoined = new RequestAdapter(getActivity(), reqJoined, "view");
                joinedView.setAdapter(requestAdapterJoined);



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

        stringRequest3.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue3.add(stringRequest3);




        return view;


    }
}
