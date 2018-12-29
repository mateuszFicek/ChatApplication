package com.example.mateusz.chatapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * Created by Mateusz on 27.12.2018.
 */

public class Chat extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ImageView profilePicture;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth = FirebaseAuth.getInstance();
        //profilePicture = (ImageView) findViewById(R.id.profileImage);
        //username = (TextView) findViewById(R.id.nameText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
