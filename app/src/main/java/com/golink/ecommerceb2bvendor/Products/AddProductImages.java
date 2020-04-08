package com.golink.ecommerceb2bvendor.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.golink.ecommerceb2bvendor.MainActivity;
import com.golink.ecommerceb2bvendor.R;
import com.golink.ecommerceb2bvendor.Registration.LogIn;
import com.golink.ecommerceb2bvendor.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddProductImages extends Fragment {

    private TextView blackNumber, whiteNumber, redNumber, greenNumber, blueNumber, previewNumber;
    private String name, moq, category, price;

    private ProgressDialog mProgress;
    private String id, usertoken;

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add_product_images, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences(LogIn.login, MODE_PRIVATE);

        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");

        Bundle b = this.getArguments();
        if(b!=null){
            name = b.getString("name");
            category = b.getString("category");
            moq = b.getString("moq");
            price = b.getString("price");
        }

        AndroidNetworking.initialize(getActivity());

        Button addBtn = view.findViewById(R.id.logButton);

        mProgress = new ProgressDialog(getActivity());
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


        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestStoragePermission();
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
                else {
                    Toast.makeText(getActivity(), "Please grant storage access from settings!", Toast.LENGTH_LONG).show();
                }


            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            .addMultipartParameter("category_id", category)
                            .addMultipartParameter("moq", moq)
                            .addMultipartParameter("price", price)
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

                    mProgress.dismiss();
                    Toast.makeText(getActivity(), "Please select Preview Image!", Toast.LENGTH_LONG).show();

                }



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



                }


            } else if (requestCode == GALLARY_REQUEST_BLACK) {


                if (data.getData() != null) {

                    Uri path = data.getData();

                    blackNumber.setText("1 File selected");

                    resultArrayBlack.add(getPath(path));



                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        blackNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayBlack.add(getPath(uri));



                        }


                    }
                }


            }

            else  if (requestCode == GALLARY_REQUEST_WHITE) {


                if (data.getData() != null) {

                    Uri path = data.getData();

                    whiteNumber.setText("1 File selected");

                    resultArrayWhite.add(getPath(path));



                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        whiteNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayWhite.add(getPath(uri));



                        }


                    }
                }


            }

            else  if (requestCode == GALLARY_REQUEST_RED) {


                if (data.getData() != null) {

                    Uri path = data.getData();

                    redNumber.setText("1 File selected");

                    resultArrayRed.add(getPath(path));



                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        redNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayRed.add(getPath(uri));



                        }


                    }
                }


            }

            else  if (requestCode == GALLARY_REQUEST_GREEN) {


                if (data.getData() != null) {

                    Uri path = data.getData();

                    greenNumber.setText("1 File selected");

                    resultArrayGreen.add(getPath(path));



                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        greenNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayGreen.add(getPath(uri));



                        }


                    }
                }


            }

            else  if (requestCode == GALLARY_REQUEST_BLUE) {


                if (data.getData() != null) {

                    Uri path = data.getData();

                    blueNumber.setText("1 File selected");

                    resultArrayBlue.add(getPath(path));



                } else {
                    if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();

                        blueNumber.setText(clipData.getItemCount() + " Files selected");

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();

                            resultArrayBlue.add(getPath(uri));



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
}
