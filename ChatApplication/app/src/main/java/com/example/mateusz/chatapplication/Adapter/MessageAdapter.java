package com.example.mateusz.chatapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mateusz.chatapplication.Chat;
import com.example.mateusz.chatapplication.Message;
import com.example.mateusz.chatapplication.Model.ChatWindow;
import com.example.mateusz.chatapplication.Model.User;
import com.example.mateusz.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Mateusz on 30.12.2018.
 * Adds new messages to currently running view.
 * Determines on which part of view should the message be showed.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<ChatWindow> Chats;
    public static final int MSG_RIGHT = 1;
    public static final int MSG_LEFT = 0;
    private String imageURL;
    int size = 0;
    FirebaseUser firebaseUser;


    public int sizeInt() {
        return size;
    }

    public MessageAdapter(Context context, List<ChatWindow> Chats, String imageURL) {
        this.context = context;
        this.Chats = Chats;
        this.imageURL = imageURL;
    }

    /**
     * Adds messages to view.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    /**
     * Adds profile image next to message.
     * @param holder - message
     * @param position - position in chat
     */
    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {

        ChatWindow chat = Chats.get(position);
        holder.show_message.setText(chat.getMessage());
        if (imageURL.equals("default")) {
            holder.profilePicture.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageURL).into(holder.profilePicture);
        }

        if (position == Chats.size() - 1) {
            if (chat.isIsseen()) {
                holder.text_seen.setText("Seen");
            } else {
                holder.text_seen.setText("Delivered");
            }
        } else {
            holder.text_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    /**
     * Keeps one item in RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView profilePicture;
        TextView text_seen;

        ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profilePicture = itemView.findViewById(R.id.profile_image);
            text_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    /**
     * Determines if message should be shown left or right.
     * @param position
     * @return - position of user who send the message
     */
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (Chats.get(position).getSendingUser().equals(firebaseUser.getUid())) {
            size += 1;
            return MSG_RIGHT;
        } else {
            size += 1;
            return MSG_LEFT;
        }
    }
}
