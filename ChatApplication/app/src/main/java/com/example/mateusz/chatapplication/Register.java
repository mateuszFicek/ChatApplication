package com.example.mateusz.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Mateusz on 27.12.2018.
 * Activity in which new user can create account.
 */

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button signButton;
    private EditText email;
    private EditText password;
    private TextView alreadyText;
    DatabaseReference reference;
    private EditText username;


    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText) findViewById(R.id.emailText);
        username = findViewById(R.id.usernameTextReg);
        password = (EditText) findViewById(R.id.passText);
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signButton).setOnClickListener(this);
        findViewById(R.id.alreadyText).setOnClickListener(this);
    }

    /**
     * Adds new user to database.
     */
    private void registerUser() {
        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();
        final String usernameString = username.getText().toString().trim();

        if (emailString.isEmpty()) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordString.isEmpty()) {
            //password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Please enter valid email address.");
            email.requestFocus();
            return;
        }

        if (passwordString.length() < 6) {
            password.setError("Minimum 6 characters.");
            password.requestFocus();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //user registered succesfully
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", usernameString);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("search", usernameString.toLowerCase());
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Registered succesfully!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Register.this, ProfileCreator.class));
                            }
                        }
                    });

                } else {
                    //user registration failure
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signButton) {
            registerUser();
        }
        if (view.getId() == R.id.alreadyText) {
            //open login activity
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
