package com.golink.ecommerceb2bvendor.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.golink.ecommerceb2bvendor.Service.GoLink.CHANNEL_1_ID;

public class MyService extends Service {

    Handler handler;
    Runnable runnable;

    private String id, usertoken, count;
    private NotificationManagerCompat notificationManagerCompat;
    SharedPreferences.Editor editor;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences2 = getSharedPreferences(LogIn.login, MODE_PRIVATE);
        editor = sharedPreferences2.edit();
        id = sharedPreferences2.getString("id", "0");
        usertoken = sharedPreferences2.getString("usertoken", "0");
        notificationManagerCompat = NotificationManagerCompat.from(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                final RequestQueue requestQueue3 = Volley.newRequestQueue(MyService.this);
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Constants.COUNT_ORDER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                            boolean error = jsonObject.getBoolean("error");

                            if(!error){

                                JSONObject obj1 = jsonObject.getJSONObject("data");
                                String count = obj1.getString("count");

                                if(count.equals("0")){
                                }else {
                                    sendNot(count);                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
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

                stringRequest3.setRetryPolicy(new DefaultRetryPolicy(10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue3.add(stringRequest3);

                handler.postDelayed(runnable, 20000);
            }
        };
        handler.post(runnable);

        return Service.START_STICKY;
    }

    private void sendNot(String count) {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LogIn.class), PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setContentTitle("GoLink")
                    .setContentText(count)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.transparent))
                    .setContentIntent(contentIntent)
                    .setNumber(Integer.parseInt(count))
                    .build();

            notificationManagerCompat.notify(1, notification);
        }

        else {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("GoLink")
                    .setContentText(count)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .setNumber(Integer.parseInt(count))
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
