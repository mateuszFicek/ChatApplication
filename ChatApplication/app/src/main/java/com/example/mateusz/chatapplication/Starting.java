package com.example.mateusz.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Mateusz on 29.12.2018.
 * Starting activity where user can choose to login or register into app.
 * Starts on every app opening but switches to Chat activity if user is already logged.
 */

public class Starting extends AppCompatActivity {

    Button signIn;
    Button signUp;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        signIn = findViewById(R.id.signInButtonStart);
        signUp = findViewById(R.id.signUpButtonStart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(Starting.this, Chat.class));
        } else {
            progressBar.setVisibility(View.GONE);
        }


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Starting.this, Register.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Starting.this, Login.class));
            }
        });
    }
}
