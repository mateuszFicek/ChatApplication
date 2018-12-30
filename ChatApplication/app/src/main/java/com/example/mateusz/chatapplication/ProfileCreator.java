package com.example.mateusz.chatapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mateusz.chatapplication.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mateusz on 28.12.2018.
 */

public class ProfileCreator extends AppCompatActivity {

    CircleImageView camera;
    private static final int CHOOSE_IMAGE = 101;
    Uri uriImage;
    String profilePictureUrl;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    StorageReference storageReference;
    String profileImageUrl;
    ProgressBar progressBar;
    private static final int IMAGE_REQUEST = 1;


    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        camera = findViewById(R.id.circlePhotoProfileCreator);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        Glide.with(getApplicationContext()).load(R.mipmap.ic_launcher).into(camera);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (!user.getImageURL().equals("default")){
                    progressBar.setVisibility(View.VISIBLE);
                    finish();
                    startActivity(new Intent(ProfileCreator.this, Chat.class));
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileCreator.this, Chat.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            try {
                Toast.makeText(ProfileCreator.this, profileImageUrl, Toast.LENGTH_SHORT).show();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                camera.setImageBitmap(bitmap);
                uploadImage();
                reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                HashMap<String, Object> map = new HashMap<>();
                map.put("imageURL", ""+profileImageUrl);
                reference.updateChildren(map);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        storageReference = FirebaseStorage.getInstance().getReference("profilepics/"+ System.currentTimeMillis()+".jpg");
        StorageTask<UploadTask.TaskSnapshot> uploadTask;
        uploadTask = storageReference.putFile(uriImage);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw  task.getException();
                }

                return  storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("imageURL", ""+mUri);
                    reference.updateChildren(map);

                } else {
                    Toast.makeText(ProfileCreator.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileCreator.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
