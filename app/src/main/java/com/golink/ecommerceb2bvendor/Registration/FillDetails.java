package com.golink.ecommerceb2bvendor.Registration;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.golink.ecommerceb2bvendor.MainActivity;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Utils.Constants;
import com.golink.ecommerceb2bvendor.Utils.Validations;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FillDetails extends AppCompatActivity {

    private String page;
    private ProgressDialog mProgress;
    private EditText profileEmailEditText, profileNameEditText, profileAddressEditText, profileBusinessEditText, profileMobileEditText, fillGst,
            profileAddressEditTextCity, profileAddressEditTextState;
    private CircleImageView fillImage;
    private String id, usertoken;
    SharedPreferences.Editor editor;
    private ArrayList<String> resultArray = new ArrayList<String>();
    private ArrayList<String> resultArrayName = new ArrayList<String>();

    private static final int STORAGE_PERMISSION_CODE =2;

    private static final int GALLARY_REQUEST = 9;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorage;
    private Uri resultUriP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_details);


        profileEmailEditText = findViewById(R.id.fillEmail);
        profileNameEditText = findViewById(R.id.fillName);
        profileBusinessEditText = findViewById(R.id.fillBusiness);
        profileMobileEditText = findViewById(R.id.fillMobile);
        profileAddressEditText = findViewById(R.id.fillAddress);
        profileAddressEditTextCity = findViewById(R.id.fillAddressCity);
        profileAddressEditTextState = findViewById(R.id.fillAddressState);
        fillGst = findViewById(R.id.fillGst);
        fillImage = findViewById(R.id.fillImage);
        mProgress = new ProgressDialog(this);

        TextView addProfile = findViewById(R.id.addProfile);

        SharedPreferences sharedPreferences3 = getSharedPreferences(LogIn.login, MODE_PRIVATE);
        id = sharedPreferences3.getString("id", "null");
        usertoken = sharedPreferences3.getString("usertoken", "null");
        editor = sharedPreferences3.edit();

        Bundle b = getIntent().getExtras();
        if(b!=null){
            page = b.getString("page");
        }


        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Vendors");
        mDatabaseUsers.keepSynced(true);
        mStorage = FirebaseStorage.getInstance().getReference();


        Button registrationUpdateButton = findViewById(R.id.logButton);

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(FillDetails.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent gallaryIntent = new Intent();
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST);
                }
                else {
                    requestStoragePermission();
                }

            }
        });



        if(page.equals("edit")){

            RequestQueue requestQueue = Volley.newRequestQueue(FillDetails.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.USER_PROFILE, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {

                    try {
                        final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                        boolean error = jsonObject.getBoolean("error");

                        if(!error){

                            JSONObject obj1 = jsonObject.getJSONObject("data");
                            String name = obj1.getString("name");
                            String email = obj1.getString("email");
                            String address = obj1.getString("address_no");
                            String city = obj1.getString("city");
                            String state = obj1.getString("state");
                            String business_name = obj1.getString("business_name");
                            String year = obj1.getString("year");
                            String gst = obj1.getString("gst");
                            final String image = obj1.getString("image_path");

                            profileNameEditText.setText(name);
                            profileEmailEditText.setText(email);
                            profileBusinessEditText.setText(business_name);
                            profileMobileEditText.setText(year);
                            profileAddressEditText.setText(address);
                            profileAddressEditTextCity.setText(city);
                            profileAddressEditTextState.setText(state);
                            fillGst.setText(gst);

                            if(!image.equals("null")){

                                Picasso.get().load((Constants.IMAGE_URL + image))
                                        .centerInside().fit()
                                        .networkPolicy(NetworkPolicy.OFFLINE).into(fillImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load((Constants.IMAGE_URL + image)).centerInside()
                                                .fit().into(fillImage);

                                    }
                                });

//                                resultUriP = Uri.parse(Constants.IMAGE_URL + image);
//                                final InputStream imageStream;
//                                try {
//                                    imageStream = getContentResolver().openInputStream(Uri.parse(Constants.IMAGE_URL + image));
//                                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
//                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//                                    byte[] b = baos.toByteArray();
//                                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//                                    resultArray.add(encodedImage);
//                                    resultArrayName.add(getFileName(Constants.IMAGE_URL + image));
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }




                            }else{
                                Picasso.get().load(R.drawable.image_prof)
                                        .centerInside().fit()
                                        .networkPolicy(NetworkPolicy.OFFLINE).into(fillImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(R.drawable.image_prof).centerInside()
                                                .fit().into(fillImage);

                                    }
                                });
                            }



                        } else {

                            String message = jsonObject.getString("message");

                            Toast.makeText(FillDetails.this, message, Toast.LENGTH_SHORT).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(FillDetails.this, "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

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




        registrationUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = profileEmailEditText.getText().toString();
                String name = profileNameEditText.getText().toString();
                String address = profileAddressEditText.getText().toString();
                String addressCity = profileAddressEditTextCity.getText().toString();
                String addressState = profileAddressEditTextState.getText().toString();
                String year = profileMobileEditText.getText().toString();
                String business = profileBusinessEditText.getText().toString();
                String gst = fillGst.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(year) && !TextUtils.isEmpty(gst)
                        && !TextUtils.isEmpty(addressCity) && !TextUtils.isEmpty(addressState)){
                    if(!TextUtils.isEmpty(email) && Validations.emailValidator(email)){

                        profile(name, email, address, year, business, gst, addressCity, addressState);

                    }else
                        Toast.makeText(FillDetails.this, "Please enter a valid Email Address", Toast.LENGTH_LONG).show();

                }else
                    Toast.makeText(FillDetails.this, "Please fill all the required fields!", Toast.LENGTH_LONG).show();

            }
        });


    }


    private void profile(final String name, final String email, final String address, final String year, final String business, final String gst,
                         final String city, final String state) {

        mProgress.setMessage("Please Wait...");
        mProgress.show();


        RequestQueue requestQueue = Volley.newRequestQueue(FillDetails.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){
                        mProgress.dismiss();

                        //JSONObject obj1 = jsonObject.getJSONObject("data");
                        //final String refer = obj1.getString("reffer_code");




                        //


                        if(resultUriP!=null){

                            mProgress.setMessage("Saving Image...");
                            mProgress.show();



                            JSONObject jsonObjectC = new JSONObject();


                            try {
                                jsonObjectC.put("userid", id);
                                jsonObjectC.put("usertoken", usertoken);

                                JSONArray array = new JSONArray();
                                for(int i = 0; i < resultArray.size(); i++){
                                    JSONObject files = new JSONObject();
                                    try {
                                        files.put("image", ("data:image/jpeg;base64," + resultArray.get(i)));
                                        files.put("filename", resultArrayName.get(i));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    array.put(files);

                                }
                                jsonObjectC.put("image_data", array);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                            RequestQueue queue = Volley.newRequestQueue(FillDetails.this);
                            JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, Constants.SAVE_IMAGE, jsonObjectC,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {


                                            try {
                                                boolean error = jsonObject.getBoolean("error");
                                                String message = jsonObject.getString("message");


                                                if(!error){

                                                    final StorageReference ref = mStorage.child("VendorsImages").child(mAuth.getCurrentUser().getUid()).child(resultUriP.getLastPathSegment());
                                                    UploadTask uploadTask = ref.putFile(resultUriP);

                                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                        @Override
                                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                            if (!task.isSuccessful()) {
                                                                throw task.getException();
                                                            }

                                                            // Continue with the task to get the download URL
                                                            return ref.getDownloadUrl();
                                                        }
                                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            if (task.isSuccessful()) {
                                                                Uri downloadUri = task.getResult();
                                                                String downloadURL = downloadUri.toString();
                                                                mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("name").setValue(business);
                                                                mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("image").setValue(downloadURL);


                                                                //editor.putString("refer_code", refer);
                                                                editor.putString("name", business);
                                                                editor.putString("email", email);
                                                                editor.apply();
                                                                mProgress.dismiss();
                                                                Intent intent = new Intent(FillDetails.this, MainActivity.class);
                                                                intent.putExtra("page", "main");
                                                                startActivity(intent);


                                                            } else {
                                                                mProgress.dismiss();
                                                                Toast.makeText(FillDetails.this, "Something went wrong.Please try again!", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });



                                                } else {

                                                    mProgress.dismiss();

                                                    Toast.makeText(FillDetails.this, message, Toast.LENGTH_SHORT).show();
                                                }

                                            } catch (JSONException e) {
                                                mProgress.dismiss();
                                                e.printStackTrace();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {

                                            mProgress.dismiss();
                                        }
                                    });

                            jobReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                            queue.add(jobReq);








                        } else {
                            if(page.equals("edit")){
                                mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("name").setValue(business);


                                Toast.makeText(FillDetails.this, "Profile successfully updated!", Toast.LENGTH_SHORT).show();
                                //editor.putString("refer_code", refer);
                                editor.putString("name", business);
                                editor.putString("email", email);
                                editor.apply();
                                mProgress.dismiss();
                                Intent intent = new Intent(FillDetails.this, MainActivity.class);
                                intent.putExtra("page", "main");
                                startActivity(intent);
                            }else{

                                Toast.makeText(FillDetails.this, "Please select an image!", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }








                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(FillDetails.this, message, Toast.LENGTH_SHORT).show();
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

                paramMap.put("usertoken", usertoken);
                paramMap.put("name", name);
                paramMap.put("email", email);
                paramMap.put("address", address + ", " + city + ", " + state);
                paramMap.put("address_no", address);
                paramMap.put("city", city);
                paramMap.put("state", state);
                paramMap.put("business_name", business);
                paramMap.put("year", year);
                paramMap.put("gst", gst);
                paramMap.put("chat_id", mAuth.getCurrentUser().getUid());
                paramMap.put("userid", id);

                return paramMap;

            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("Gallery access is required to select an image!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FillDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(FillDetails.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GALLARY_REQUEST);
            }
            else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLARY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri path = result.getUri();

                resultUriP = path;
                fillImage.setImageURI(path);

                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(path);
                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    resultArray.add(encodedImage);
                    resultArrayName.add(getFileName(path.toString()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public String getFileName(String path) {

        Uri uri = Uri.parse(path);
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;

    }


}
