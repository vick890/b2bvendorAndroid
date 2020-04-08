package com.golink.ecommerceb2bvendor.Products;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.golink.ecommerceb2bvendor.Registration.OtpPage;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCategory extends AppCompatActivity {

    private ProgressDialog mProgress;
    private String id, usertoken;
    private EditText addCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        SharedPreferences sharedPreferences2 = getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        mProgress = new ProgressDialog(this);

        addCategory = findViewById(R.id.enterText);
        Button enterButton  = findViewById(R.id.enterButton);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cat = addCategory.getText().toString();

                if(!TextUtils.isEmpty(cat)){
                    mProgress.setMessage("Please Wait..");
                    mProgress.show();

                    RequestQueue requestQueue = Volley.newRequestQueue(AddCategory.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SAVE_CAT, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {

                            try {
                                final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                                boolean error = jsonObject.getBoolean("error");

                                if(!error){

                                    Toast.makeText(AddCategory.this, "Category added successfully!", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(AddCategory.this, MainActivity.class);
                                    intent.putExtra("page", "category");
                                    startActivity(intent);
                                    finish();

                                    mProgress.dismiss();


                                } else {

                                    String message = jsonObject.getString("message");

                                    Toast.makeText(AddCategory.this, message, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> paramMap = new HashMap<String, String>();

                            paramMap.put("userid", id);
                            paramMap.put("usertoken", usertoken);
                            paramMap.put("name", cat);
                            paramMap.put("image[]", "");

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



    }
}
