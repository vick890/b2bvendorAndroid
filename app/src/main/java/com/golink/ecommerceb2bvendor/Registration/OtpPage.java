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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpPage extends AppCompatActivity {

    private EditText registrationOtpEditText;
    private ProgressDialog mProgress;
    SharedPreferences.Editor editor;
    private String check;
    private String id, usertoken, otpOld, phone;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_page);

        SharedPreferences sharedPreferences2 = getSharedPreferences(LogIn.login, MODE_PRIVATE);
        editor = sharedPreferences2.edit();

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");
        otpOld = sharedPreferences2.getString("otp", "0000");


        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Vendors");
        mDatabaseUsers.keepSynced(true);

        registrationOtpEditText = findViewById(R.id.logPhone);
        mProgress = new ProgressDialog(this);

        Button registrationConfirmButton = findViewById(R.id.logButton);
        final TextView resendOtp = findViewById(R.id.resendotp);

        Bundle b = getIntent().getExtras();
        if(b!=null){
            check = b.getString("check");
            phone = b.getString("phone");
        }


        final ImageView logo = findViewById(R.id.logo);

        Picasso.get().load((R.drawable.otp80))
                .centerInside().fit()
                .networkPolicy(NetworkPolicy.OFFLINE).into(logo, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load((R.drawable.otp80)).centerInside()
                        .fit().into(logo);

            }
        });


        registrationConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp = registrationOtpEditText.getText().toString();

                if(!TextUtils.isEmpty(otp)){
                    if(Validations.isValidMobile(otp, 4)){
                        otpEnter(otp);
                    }else{
                        Toast.makeText(OtpPage.this, "Please enter a valid 4 digit OTP", Toast.LENGTH_LONG).show();
                    }
                }else
                    Toast.makeText(OtpPage.this, "Please enter a valid 4 digit OTP", Toast.LENGTH_LONG).show();



            }
        });


        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resend();

            }
        });


    }

    private void resend() {

        mProgress.setMessage("Sending! Please Wait...");
        mProgress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(OtpPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.RESEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        Toast.makeText(OtpPage.this, "OTP sent successfully!", Toast.LENGTH_LONG).show();

                        mProgress.dismiss();


                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(OtpPage.this, message, Toast.LENGTH_SHORT).show();
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

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);


    }


    private void otpEnter(final String otp) {

        mProgress.setMessage("Verifying! Please Wait...");
        mProgress.show();

        RequestQueue requestQueue = Volley.newRequestQueue(OtpPage.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        JSONObject obj1 = jsonObject.getJSONObject("data");
                        String refer_code = obj1.getString("reffer_code");

                        editor.putBoolean("saveLogin", true);
                        editor.putString("refer", refer_code);
                        editor.apply();



                        if(check.equals("login")){
                            login();
                        }else{
                            signup();
                        }



                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(OtpPage.this, message, Toast.LENGTH_SHORT).show();
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

                paramMap.put("otp", otp);
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


    private void signup(){

        mAuth.createUserWithEmailAndPassword(("vendor" + phone + "@gmail.com"), (phone))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!= null){
                                DatabaseReference databaseReference = mDatabaseUsers.child(user.getUid());
                                databaseReference.child("email").setValue("vendor" + phone + "@gmail.com");
                                databaseReference.child("phone").setValue(phone);
                                mProgress.dismiss();

                                Intent intentLog = new Intent(OtpPage.this, FillDetails.class);
                                intentLog.putExtra("page", "otp");
                                startActivity(intentLog);
                                finish();

                            }
                        } else {
                            Toast.makeText(OtpPage.this, "Registration failed! " + "\n" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        mProgress.dismiss();
                    }
                });

    }



    private void login(){

        mAuth.signInWithEmailAndPassword(("vendor" + phone + "@gmail.com"), (phone))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser user = mAuth.getCurrentUser();

                            mProgress.dismiss();

                            Intent intent = new Intent(OtpPage.this, MainActivity.class);
                            intent.putExtra("page", "main");
                            startActivity(intent);
                            finish();




                        } else {

                            mProgress.dismiss();

                            Toast.makeText(OtpPage.this, "Please try again!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }



}
