package com.golink.ecommerceb2bvendor.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChat extends AppCompatActivity {

    private String uid;
    private EditText chatMsg;

    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    PersonalAdapter personalAdapter;
    RecyclerView groupView2;
    private CircleImageView registerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        chatMsg = (EditText) findViewById(R.id.chatMsg);
        ImageButton sendBtn = (ImageButton) findViewById(R.id.sendBtn);
        final TextView registerHead = (TextView) findViewById(R.id.registerHead);
        registerImage = (CircleImageView) findViewById(R.id.registerImage);

        Intent key = getIntent();
        Bundle b = key.getExtras();

        if (b != null) {
            uid = (String) b.get("uid");
        }

        personalAdapter = new PersonalAdapter();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();


        groupView2 = (RecyclerView) findViewById(R.id.groupView2);
        groupView2.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        groupView2.setLayoutManager(layoutManager);
        groupView2.setAdapter(personalAdapter);




        chatFn();

        mDatabaseUsers.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                final String image = dataSnapshot.child("image").getValue(String.class);
                registerHead.setText(name);
                Picasso.get().load(image)
                        .centerInside().resize(1000,1000)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(registerImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).centerInside()
                                .resize(1000,1000).into(registerImage);
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = chatMsg.getText().toString();

                if(!TextUtils.isEmpty(msg)){

                    String timeStamp = String.valueOf(System.currentTimeMillis());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                    final String tossDate = simpleDateFormat.format(new Date());
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
                    final String tossTime = simpleDateFormat1.format(new Date());

                    ChatItems data = new ChatItems();
                    data.setId(mAuth.getCurrentUser().getUid());
                    data.setName("");
                    data.setMessage(msg);
                    data.setDate(tossDate);
                    data.setTime(tossTime);

                    mDatabaseUsers.child("personal").child(mAuth.getCurrentUser().getUid()).child(uid).child(timeStamp).setValue(data);
                    mDatabaseUsers.child("personal").child(uid).child(mAuth.getCurrentUser().getUid()).child(timeStamp).setValue(data);
                    chatMsg.setText("");

                }

            }
        });


    }

    private void chatFn(){

        mDatabaseUsers.child("personal").child(mAuth.getCurrentUser().getUid()).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personalAdapter.clearData();

                for (DataSnapshot d : dataSnapshot.getChildren()){

                    ChatItems chatItems = d.getValue(ChatItems.class);
                    personalAdapter.addData(chatItems);

                }

                groupView2.smoothScrollToPosition(personalAdapter.getItemCount());



                personalAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

