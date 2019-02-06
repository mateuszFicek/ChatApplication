package com.example.mateusz.chatapplication.Fragments;

import com.example.mateusz.chatapplication.Model.ChatWindow;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mateusz.chatapplication.Adapter.UserAdapter;
import com.example.mateusz.chatapplication.Chat;
import com.example.mateusz.chatapplication.Model.ChatWindow;
import com.example.mateusz.chatapplication.Model.User;
import com.example.mateusz.chatapplication.Notification.Token;
import com.example.mateusz.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Chat fragment used in Chat activity.
 * Shows all users to whom user had send message.
 */

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private List<String> userStrings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userStrings = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userStrings.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatWindow chat = snapshot.getValue(ChatWindow.class);
                    if (chat.getSendingUser().equals(firebaseUser.getUid())) {
                        userStrings.add(chat.getRecievingUser());
                    }
                    if (chat.getRecievingUser().equals(firebaseUser.getUid())) {
                        userStrings.add(chat.getSendingUser());
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

    /**
     * Gets all users to who user send message.
     * Adds them to list which is used to create recyclerView.
     */
    private void readChats() {
        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    Set<String> set = new HashSet<>(userStrings);
                    userStrings.clear();
                    userStrings.addAll(set);

                    for (String id : set) {
                        if (set.contains(user.getId())) {
                            if (!userList.contains(user)) {
                                userList.add(user);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), userList, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
