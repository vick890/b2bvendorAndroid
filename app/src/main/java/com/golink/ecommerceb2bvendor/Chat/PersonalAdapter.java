package com.golink.ecommerceb2bvendor.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.golink.ecommerceb2bvendor.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.ChatViewHolder> {

    private FirebaseAuth mAuth;

    /**
     * ViewHolder to be the item of the list
     */
    static final class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        TextView message2;
        CardView cardView;
        CardView cardView2;
        TextView time;
        TextView time2;

        ChatViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cardView);
            cardView2 = (CardView) view.findViewById(R.id.cardView2);
            message = (TextView) view.findViewById(R.id.message);
            message2 = (TextView) view.findViewById(R.id.message2);
            time = (TextView) view.findViewById(R.id.time);
            time2 = (TextView) view.findViewById(R.id.time2);
        }
    }

    private List<ChatItems> mContent = new ArrayList<>();

    public void clearData() {
        mContent.clear();
    }

    public void addData(ChatItems data) {
        mContent.add(data);
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_items, parent, false);
        return new ChatViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatItems data = mContent.get(position);

        mAuth = FirebaseAuth.getInstance();

        if (data.getId().equals(mAuth.getCurrentUser().getUid())) {
            holder.cardView2.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);

            holder.message2.setText(data.getMessage());
            holder.time2.setText(data.getTime());
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.cardView2.setVisibility(View.GONE);

            holder.message.setText(data.getMessage());
            holder.time.setText(data.getTime());
        }


    }


}

