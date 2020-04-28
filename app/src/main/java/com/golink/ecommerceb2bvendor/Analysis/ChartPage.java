package com.golink.ecommerceb2bvendor.Analysis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.golink.ecommerceb2bvendor.MainActivity;
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

public class ChartPage extends Fragment {

    private String id, usertoken;
    private ProgressBar progressD;
    private AnyChartView anyChartView;
    private String val;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_chart_page, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");


        progressD = view.findViewById(R.id.progressD);

        anyChartView = view.findViewById(R.id.any_chart_view);
//        anyChartViewQ = view.findViewById(R.id.any_chart_viewQ);
//        anyChartViewY = view.findViewById(R.id.any_chart_viewY);

        final Button allBtn = view.findViewById(R.id.allBtn);
        final Button pendingBtn = view.findViewById(R.id.pendingBtn);
        final Button joinedBtn = view.findViewById(R.id.joinedBtn);

//        final RelativeLayout mLay = view.findViewById(R.id.mLay);
//        final RelativeLayout qLay = view.findViewById(R.id.qLay);
//        final RelativeLayout yLay = view.findViewById(R.id.yLay);

        Bundle b = this.getArguments();
        if(b!=null){
            val = b.getString("page");
            assert val != null;
            switch (val) {
                case "month":
                    allBtn.setBackgroundResource(R.color.colorAccent);
                    allBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    pendingBtn.setBackgroundResource(R.color.colorWhite);
                    pendingBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    joinedBtn.setBackgroundResource(R.color.colorWhite);
                    joinedBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    monthly();
                    break;
                case "quarter":
                    joinedBtn.setBackgroundResource(R.color.colorAccent);
                    joinedBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    allBtn.setBackgroundResource(R.color.colorWhite);
                    allBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    pendingBtn.setBackgroundResource(R.color.colorWhite);
                    pendingBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    quarterly();
                    break;
                case "year":
                    pendingBtn.setBackgroundResource(R.color.colorAccent);
                    pendingBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    allBtn.setBackgroundResource(R.color.colorWhite);
                    allBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    joinedBtn.setBackgroundResource(R.color.colorWhite);
                    joinedBtn.setTextColor(Color.parseColor("#1A7BA8"));
                    yearly();
                    break;
            }
        }else{
            monthly();
           // yearly();
        }

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allBtn.setBackgroundResource(R.color.colorAccent);
                allBtn.setTextColor(Color.parseColor("#FFFFFF"));
                pendingBtn.setBackgroundResource(R.color.colorWhite);
                pendingBtn.setTextColor(Color.parseColor("#1A7BA8"));
                joinedBtn.setBackgroundResource(R.color.colorWhite);
                joinedBtn.setTextColor(Color.parseColor("#1A7BA8"));
//                mLay.setVisibility(View.VISIBLE);
//                qLay.setVisibility(View.GONE);
//                yLay.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("page", "month");
                startActivity(intent);
                getActivity().finish();


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

//                mLay.setVisibility(View.GONE);
//                yLay.setVisibility(View.VISIBLE);
//                qLay.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("page", "year");
                startActivity(intent);
                getActivity().finish();

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

//                mLay.setVisibility(View.GONE);
//                qLay.setVisibility(View.VISIBLE);
//                yLay.setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("page", "quarter");
                startActivity(intent);
                getActivity().finish();

            }
        });

        return view;

    }


    private void monthly() {
        progressD.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MONTHLY_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        List<DataEntry> data = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                            data.add(new ValueDataEntry(jsonObject2.getString("month_name"),
                                    Long.parseLong(jsonObject2.getString("total_amount_val"))));

                        }
                        Cartesian cartesian = AnyChart.column();

                        Column column = cartesian.column(data);

                        column.tooltip()
                                .titleFormat("{₹X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("₹{%Value}{groupsSeparator: }");

                        cartesian.animation(true);
                        //cartesian.title("Monthly Sales");

                        cartesian.yScale().minimum(0d);

                        cartesian.yAxis(0).labels().format("₹{%Value}{groupsSeparator: }");

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.xAxis(0).title("Months");
                        cartesian.yAxis(0).title("Total Sales");


                        anyChartView.setChart(cartesian);

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

    }


    private void quarterly() {
        progressD.setVisibility(View.VISIBLE);

        final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.QUARTERLY_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        List<DataEntry> data = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);



                            data.add(new ValueDataEntry(jsonObject2.getString("year") + " Q" + jsonObject2.getString("quarter"),
                                    Long.parseLong(jsonObject2.getString("total_amount_val"))));



                        }
                        Cartesian cartesian = AnyChart.column();

                        Column column = cartesian.column(data);

                        column.tooltip()
                                .titleFormat("{₹X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("₹{%Value}{groupsSeparator: }");

                        cartesian.animation(true);
                        //cartesian.title("Monthly Sales");

                        cartesian.yScale().minimum(0d);

                        cartesian.yAxis(0).labels().format("₹{%Value}{groupsSeparator: }");

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.xAxis(0).title("Quarters");
                        cartesian.yAxis(0).title("Total Sales");

                        anyChartView.setChart(cartesian);
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

        stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue2.add(stringRequest2);

    }


    private void yearly (){
        progressD.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.YEARLY_REPORT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        List<DataEntry> data = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            data.add(new ValueDataEntry(jsonObject2.getString("year"),
                                    Long.parseLong(jsonObject2.getString("total_amount_val"))));
                        }

                        Cartesian cartesian = AnyChart.column();

                        Column column = cartesian.column(data);

                        column.tooltip()
                                .titleFormat("{₹X}")
                                .position(Position.CENTER_BOTTOM)
                                .anchor(Anchor.CENTER_BOTTOM)
                                .offsetX(0d)
                                .offsetY(5d)
                                .format("₹{%Value}{groupsSeparator: }");

                        cartesian.animation(true);
                        //cartesian.title("Monthly Sales");

                        cartesian.yScale().minimum(0d);

                        cartesian.yAxis(0).labels().format("₹{%Value}{groupsSeparator: }");

                        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                        cartesian.interactivity().hoverMode(HoverMode.BY_X);

                        cartesian.xAxis(0).title("Years");
                        cartesian.yAxis(0).title("Total Sales");

                        anyChartView.setChart(cartesian);

                    }
                }
                catch (JSONException e) {

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

        stringRequest3.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue3.add(stringRequest3);
    }

}
