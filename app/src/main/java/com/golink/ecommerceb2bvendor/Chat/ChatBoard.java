package com.golink.ecommerceb2bvendor.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBoard extends Fragment {

    FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    PeopleAdapter peopleAdapter;
    RecyclerView peopleView;

    List<String> people = new ArrayList<>();
    private ProgressBar progressD;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_chat_board, container, false);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle("");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();


        peopleView = (RecyclerView) view.findViewById(R.id.peopleView);
        peopleView.setHasFixedSize(true);
        ImageButton vendorAll = view.findViewById(R.id.vendorAll);

        progressD = view.findViewById(R.id.progressD);
        progressD.setVisibility(View.VISIBLE);

        people.clear();

        mDatabaseUsers.child("personal").child(mAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                people.add(key);

                peopleAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                peopleAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        peopleView.setLayoutManager(layoutManager);
        peopleAdapter = new PeopleAdapter(getActivity(), people);
        peopleView.setAdapter(peopleAdapter);
        progressD.setVisibility(View.GONE);




        vendorAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new AllVendors();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_second, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });


        return view;


    }
}


class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.RecyclerViewHolder> {

    private Context mCtx;
    private List<String> people;
    private DatabaseReference mDatabaseUsers;

    public PeopleAdapter(Context mCtx, List<String> people) {
        this.mCtx = mCtx;
        this.people = people;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.people_items, null);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final String uid = people.get(position);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.keepSynced(true);

        mDatabaseUsers.child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                final String image = dataSnapshot.child("image").getValue(String.class);

                holder.keeratText.setText(name);

                Picasso.get().load(image)
                        .centerInside().resize(1000,1000)
                        .networkPolicy(NetworkPolicy.OFFLINE).into(holder.keeratImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).centerInside()
                                .resize(1000,1000).into(holder.keeratImage);
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.boardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mCtx, PersonalChat.class);
                intent.putExtra("uid", uid);
                mCtx.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView keeratImage;
        private TextView keeratText;
        private RelativeLayout boardLayout;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            keeratImage = itemView.findViewById(R.id.keeratImage);
            keeratText = itemView.findViewById(R.id.keeratText);
            boardLayout = itemView.findViewById(R.id.boardLayout);


        }
    }
}

