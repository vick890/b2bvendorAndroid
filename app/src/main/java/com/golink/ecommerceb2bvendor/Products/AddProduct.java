package com.golink.ecommerceb2bvendor.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
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
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.golink.ecommerceb2bvendor.MainActivity;
import com.golink.ecommerceb2bvendor.PreviewImages;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddProduct extends Fragment {

    private ScrollView scrollView;
    private EditText prodName, prodMoq, prodPrice,offerPrice;
    private Spinner prodType;
    private Context context;
    final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    //private ImageView previewImagess;

    private List<String> manufacture = new ArrayList<>();
    private HashMap<String, String> manufactureCheck= new HashMap<String, String>();
    private HashMap<String, String> manufactureCheckEdit= new HashMap<String, String>();
    private ProgressDialog mProgress;
    private String id, usertoken, checkPage, feedkey;
    private CheckBox outOfStockCheckBox;
    private TextView previewImages;
    private TextView blackNumber, whiteNumber, redNumber, greenNumber, blueNumber, previewNumber;

    private static final int GALLARY_REQUEST_BLACK = 1;
    private static final int GALLARY_REQUEST_WHITE = 2;
    private static final int GALLARY_REQUEST_RED = 3;
    private static final int GALLARY_REQUEST_GREEN = 4;
    private static final int GALLARY_REQUEST_BLUE = 5;
    private static final int GALLARY_REQUEST_PREVIEW = 6;

    private ArrayList<String> resultArrayBlack = new ArrayList<String>();
    private ArrayList<String> resultArrayWhite = new ArrayList<String>();
    private ArrayList<String> resultArrayRed = new ArrayList<String>();
    private ArrayList<String> resultArrayGreen = new ArrayList<String>();
    private ArrayList<String> resultArrayBlue = new ArrayList<String>();
    private ArrayList<String> resultArrayPreview = new ArrayList<String>();


    public AddProduct(Context context) {
        this.context = context;
    }

    public AddProduct() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_product, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        Bundle b = this.getArguments();
        if(b!=null){
            checkPage = b.getString("checkPage");
            feedkey = b.getString("feedkey");
        }

        scrollView = view.findViewById(R.id.scrollView);

        Button addBtn = view.findViewById(R.id.logButton);
        prodName = view.findViewById(R.id.prodName);
        prodType = view.findViewById(R.id.prodType);
        prodMoq = view.findViewById(R.id.prodMoq);
        prodPrice = view.findViewById(R.id.prodPrice);
        offerPrice = view.findViewById(R.id.offerPrice);
        mProgress = new ProgressDialog(getActivity());
        Button addCat = view.findViewById(R.id.addCat);
        outOfStockCheckBox = view.findViewById(R.id.outOfStockCheckBox);
        previewImages = view.findViewById(R.id.previewImages);
     //   previewImagess = view.findViewById(R.id.previewImagess);

        mProgress.setMessage("Please Wait...");
        mProgress.show();

        AndroidNetworking.initialize(getActivity());

        TextView blackImage = view.findViewById(R.id.blackImage);
        TextView whiteImage = view.findViewById(R.id.whiteImage);
        TextView redImage = view.findViewById(R.id.redImage);
        TextView greenImage = view.findViewById(R.id.greenImage);
        TextView blueImage = view.findViewById(R.id.blueImage);
        TextView previewImage = view.findViewById(R.id.previewImage);

        blackNumber = view.findViewById(R.id.blackNumber);
        whiteNumber = view.findViewById(R.id.whiteNumber);
        redNumber = view.findViewById(R.id.redNumber);
        greenNumber = view.findViewById(R.id.greenNumber);
        blueNumber = view.findViewById(R.id.blueNumber);
        previewNumber = view.findViewById(R.id.previewNumber);

       /* previewNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blueNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        greenNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        redNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        whiteNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmaps != null){
                    previewImagess.setVisibility(View.VISIBLE);
                    showImages();
                }
                else{
                    Toast.makeText(context, "First Select Some Images...", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestStoragePermission();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ALL_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    boolean error = jsonObject.getBoolean("error");

                    if(!error){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                            manufacture.add(jsonObject2.getString("name"));
                            manufactureCheck.put(jsonObject2.getString("name"), jsonObject2.getString("category_id"));
                            manufactureCheckEdit.put(jsonObject2.getString("category_id"), jsonObject2.getString("name"));

                        }

                        if(getActivity()!=null){

                            ArrayAdapter menLISt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, manufacture);
                            menLISt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            prodType.setAdapter(menLISt);
                        }

                    } else {

                        String message = jsonObject.getString("message");

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //mProgress.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //mProgress.dismiss();
                //Toast.makeText(getActivity(), "Some error occured. Please Try Again!", Toast.LENGTH_SHORT).show();

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






        if(checkPage.equals("edit")){

            final RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, Constants.PRODUCT_DETAIl, new Response.Listener<String>() {
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
                            final String category = obj1.getString("category_id");
                            final String ofs = obj1.getString("out_of_stock");
                            final String offer_price = obj1.getString("offer_price");

                            prodName.setText(name);
                            prodPrice.setText(price);
                            offerPrice.setText(offer_price);
                            prodMoq.setText(moq);
                            if (ofs.equals("1")){
                                outOfStockCheckBox.setChecked(true);
                            }
                            else{
                                outOfStockCheckBox.setChecked(false);
                            }
                            String manFinal = manufactureCheckEdit.get(category);
                            selectSpinnerValue(prodType, manFinal);
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

            stringRequest2.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue2.add(stringRequest2);
        }else{
            mProgress.dismiss();
        }

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent gallaryIntent = new Intent();
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_PREVIEW);
                }
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }


            }
        });

        blackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent gallaryIntent = new Intent();
                    gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_BLACK);
                }
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }
            }
        });

        whiteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent gallaryIntent = new Intent();
                    gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_WHITE);
                }
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }


            }
        });

        redImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent gallaryIntent = new Intent();
                    gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_RED);
                }
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }


            }
        });

        greenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent gallaryIntent = new Intent();
                    gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_GREEN);
                }
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }


            }
        });

        blueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent gallaryIntent = new Intent();
                    gallaryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    gallaryIntent.setType("image/*");
                    startActivityForResult(gallaryIntent, GALLARY_REQUEST_BLUE);
                }
                else
                    {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ofs;
                if (outOfStockCheckBox.isChecked()){
                    ofs = "1";
                }
                else{
                    ofs = "0";
                }

                String man = prodType.getSelectedItem().toString();
                Toast.makeText(getActivity(), man, Toast.LENGTH_SHORT).show();
                String manFinal = manufactureCheck.get(man);
                Toast.makeText(getActivity(), manFinal, Toast.LENGTH_SHORT).show();


                String name = prodName.getText().toString();
                String moq = prodMoq.getText().toString();
                String price = prodPrice.getText().toString();
                String ooo = offerPrice.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(moq) && !TextUtils.isEmpty(price) && !TextUtils.isEmpty(ooo)){



                    mProgress.setMessage("Please Wait...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();

                    List<File> preview = new ArrayList<File>();
                    for(int i = 0; i< resultArrayPreview.size(); i++){
                        preview.add(new File(resultArrayPreview.get(i)));
                    }
                    List<File> black = new ArrayList<File>();
                    for(int i = 0; i< resultArrayBlack.size(); i++){
                        black.add(new File(resultArrayBlack.get(i)));
                    }
                    List<File> white = new ArrayList<File>();
                    for(int i = 0; i< resultArrayWhite.size(); i++){
                        white.add(new File(resultArrayWhite.get(i)));
                    }
                    List<File> red = new ArrayList<File>();
                    for(int i = 0; i< resultArrayRed.size(); i++){
                        red.add(new File(resultArrayRed.get(i)));
                    }
                    List<File> green = new ArrayList<File>();
                    for(int i = 0; i< resultArrayGreen.size(); i++){
                        green.add(new File(resultArrayGreen.get(i)));
                    }
                    List<File> blue = new ArrayList<File>();
                    for(int i = 0; i< resultArrayBlue.size(); i++){
                        blue.add(new File(resultArrayBlue.get(i)));
                    }

                    if(checkPage.equals("edit")){

                        AndroidNetworking.upload(Constants.SAVE_PRODUCT)
                                .addMultipartFileList("preview_image[]", preview)
                                .addMultipartFileList("black[]", black)
                                .addMultipartFileList("white[]", white)
                                .addMultipartFileList("red[]", red)
                                .addMultipartFileList("green[]", green)
                                .addMultipartFileList("blue[]", blue)
                                .addMultipartParameter("userid", id)
                                .addMultipartParameter("usertoken", usertoken)
                                .addMultipartParameter("name", name)
                                .addMultipartParameter("category_id", manFinal)
                                .addMultipartParameter("moq", moq)
                                .addMultipartParameter("price", price)
                                .addMultipartParameter("product_id", feedkey)
                                .addMultipartParameter("out_of_stock", ofs)
                                .addMultipartParameter("offer_price", ooo)
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        // do anything with progress
                                    }
                                })
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // do anything with response
                                        try {
                                            boolean error = response.getBoolean("error");

                                            if(!error){

                                                mProgress.dismiss();
                                                Toast.makeText(getActivity(), "Successfully Saved!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                intent.putExtra("page", "product");
                                                startActivity(intent);


                                            } else {

                                                String message = response.getString("message");

                                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                mProgress.dismiss();

                                            }

                                        } catch (JSONException e) {
                                            mProgress.dismiss();
                                            e.printStackTrace();
                                        }


                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        mProgress.dismiss();
                                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }else{
                        if(preview.size()!=0){
                            AndroidNetworking.upload(Constants.SAVE_PRODUCT)
                                    .addMultipartFileList("preview_image[]", preview)
                                    .addMultipartFileList("black[]", black)
                                    .addMultipartFileList("white[]", white)
                                    .addMultipartFileList("red[]", red)
                                    .addMultipartFileList("green[]", green)
                                    .addMultipartFileList("blue[]", blue)
                                    .addMultipartParameter("userid", id)
                                    .addMultipartParameter("usertoken", usertoken)
                                    .addMultipartParameter("name", name)
                                    .addMultipartParameter("category_id", manFinal)
                                    .addMultipartParameter("moq", moq)
                                    .addMultipartParameter("price", price)
                                    .addMultipartParameter("out_of_stock", ofs)
                                    .addMultipartParameter("offer_price", ooo)
                                    .setPriority(Priority.HIGH)
                                    .build()
                                    .setUploadProgressListener(new UploadProgressListener() {
                                        @Override
                                        public void onProgress(long bytesUploaded, long totalBytes) {
                                            // do anything with progress
                                        }
                                    })
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // do anything with response
                                            try {
                                                boolean error = response.getBoolean("error");

                                                if(!error){


                                                    mProgress.dismiss();
                                                    Toast.makeText(getActivity(), "Successfully Added!", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    intent.putExtra("page", "product");
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    String message = response.getString("message");

                                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                    mProgress.dismiss();
                                                }
                                            } catch (JSONException e) {
                                                mProgress.dismiss();
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError error) {
                                            // handle error
                                            mProgress.dismiss();
                                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }else{

                            mProgress.dismiss();
                            Toast.makeText(getActivity(), "Please select Preview Image!", Toast.LENGTH_LONG).show();

                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "Required field(s) empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCategory.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLARY_REQUEST_PREVIEW) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    previewNumber.setText("1 File selected");
                    resultArrayPreview.add(getPath(path));
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == GALLARY_REQUEST_BLACK) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    blackNumber.setText("1 File selected");
                    resultArrayBlack.add(getPath(path));

                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        blackNumber.setText(clipData.getItemCount() + " Files selected");
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            resultArrayBlack.add(getPath(uri));

                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }

            else  if (requestCode == GALLARY_REQUEST_WHITE) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    whiteNumber.setText("1 File selected");
                    resultArrayWhite.add(getPath(path));
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        whiteNumber.setText(clipData.getItemCount() + " Files selected");
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayWhite.add(getPath(uri));
                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            else  if (requestCode == GALLARY_REQUEST_RED) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    redNumber.setText("1 File selected");
                    resultArrayRed.add(getPath(path));
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        redNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            resultArrayRed.add(getPath(uri));
                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            else  if (requestCode == GALLARY_REQUEST_GREEN) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    greenNumber.setText("1 File selected");
                    resultArrayGreen.add(getPath(path));
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        greenNumber.setText(clipData.getItemCount() + " Files selected");
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            resultArrayGreen.add(getPath(uri));
                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            else  if (requestCode == GALLARY_REQUEST_BLUE) {
                if (data.getData() != null) {
                    Uri path = data.getData();
                    blueNumber.setText("1 File selected");
                    resultArrayBlue.add(getPath(path));
                    try {
                        InputStream is = getActivity().getContentResolver().openInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        blueNumber.setText(clipData.getItemCount() + " Files selected");
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            resultArrayBlue.add(getPath(uri));
                            try {
                                InputStream is = getActivity().getContentResolver().openInputStream(uri);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                bitmaps.add(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getPath(Uri contentUri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null){
            return null;
        }else{
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s=cursor.getString(column_index);
            cursor.close();
            if(s!= null){
                return s;
            }else{
                String wholeID = DocumentsContract.getDocumentId(contentUri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = { MediaStore.Images.Media.DATA };

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                cursor = getActivity().getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{ id }, null);

                String filePath = "";

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return  filePath;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission Required")
                    .setMessage("Gallery access is required to select an image!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 21);
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 21);
        }
    }



    private void selectSpinnerValue(Spinner spinner, String myString) {
        int index = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }

   /* private void showImages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0,previewImagess.getTop());
                for (final Bitmap b: bitmaps){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previewImagess.setImageBitmap(b);
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }*/

}



