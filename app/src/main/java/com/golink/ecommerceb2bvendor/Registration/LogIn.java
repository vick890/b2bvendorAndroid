package com.golink.ecommerceb2bvendor.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.golink.ecommerceb2bvendor.Utils.Validations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    private EditText registrationNumberEditText;
    private ProgressDialog mProgress;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;
    Boolean saveLogin;
    Boolean loggedOut = false;
    public static String login = "com.ecomv.login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        sharedPreferences2 = getSharedPreferences(login, MODE_PRIVATE);
        editor = sharedPreferences2.edit();

        Intent key = getIntent();
        Bundle b = key.getExtras();

        if (b != null) {
            loggedOut = (Boolean) b.get("loggedOut");

            if (loggedOut != null) {

                if (loggedOut) {
                    editor.putBoolean("saveLogin", false);
                    editor.apply();
                }

            }

        }


        saveLogin = sharedPreferences2.getBoolean("saveLogin", false);
        if (saveLogin) {

            Intent intent = new Intent(LogIn.this, MainActivity.class);
            intent.putExtra("page", "main");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }

        registrationNumberEditText = findViewById(R.id.logPhone);
        Button registrationSignButton = findViewById(R.id.logButton);
        //TextView loginLogin = findViewById(R.id.createText);
        mProgress = new ProgressDialog(this);

//        final ImageView logo = findViewById(R.id.logo);
//
//
//        Picasso.get().load(R.drawable.app_logo)
//                .centerInside().resize(500, 500)
//                .networkPolicy(NetworkPolicy.OFFLINE).into(logo, new Callback() {
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Picasso.get().load(R.drawable.app_logo).centerInside()
//                        .resize(500, 500).into(logo);
//
//            }
//        });


//        loginLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(LogIn.this, SignUp.class);
//                startActivity(intent);
//
//            }
//        });

        final ImageView logo = findViewById(R.id.logo);

        Picasso.get().load((R.drawable.logo_icon))
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(logo, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load((R.drawable.logo_icon)).centerInside()
                        .fit().into(logo);

            }
        });


        registrationSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = registrationNumberEditText.getText().toString();

                if(!TextUtils.isEmpty(phone) && Validations.isValidMobile(phone, 10)){
                    login(phone);
                }
                else
                    Toast.makeText(LogIn.this, "Please enter a valid 10 digit mobile number", Toast.LENGTH_LONG).show();


            }
        });



    }


    private void login(final String phone) {

        mProgress.setMessage("Sending OTP...");
        mProgress.show();


        RequestQueue requestQueue = Volley.newRequestQueue(LogIn.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONObject obj1 = jsonObject.getJSONObject("data");
                        String userid = obj1.getString("userid");
                        String usertoken = obj1.getString("usertoken");
                        String otp = obj1.getString("otp");
                        String check = obj1.getString("check");

                        editor.putString("id", userid);
                        editor.putString("usertoken", usertoken);
                        editor.putString("otp", otp);
                        editor.putString("phone", phone);
                        editor.apply();

                        Intent intent = new Intent(LogIn.this, OtpPage.class);
                        intent.putExtra("check", check);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();


                        mProgress.dismiss();


                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(LogIn.this, message, Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramMap = new HashMap<String, String>();

                paramMap.put("mobile", phone);

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);



    }


    @Override
    protected void onStart() {
        super.onStart();
        saveLogin = sharedPreferences2.getBoolean("saveLogin", false);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        super.onBackPressed();
    }


}
