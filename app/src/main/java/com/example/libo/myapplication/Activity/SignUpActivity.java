package com.example.libo.myapplication.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;

    private static int REQUESCODE = 1;

    EditText editTextUsername,editTextPassword,editTextEmail;
    DatabaseReference databaseUser;
    private FirebaseAuth mAuth;
    private ImageView photo;
    private Uri uriProfileImage;
    private DatabaseReference usernameRef;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        usernameRef = FirebaseDatabase.getInstance().getReference("username");
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        findViewById(R.id.buttonCancel).setOnClickListener(this);


        photo = (ImageView) findViewById(R.id.userphoto);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opeangalley();
            }
        });

    }

    private void opeangalley() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }


    private void RegisterUser(){
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();



        if (username.isEmpty()){

            editTextUsername.setError("Username is required");

            editTextUsername.requestFocus();

        }

        if (password.isEmpty()){

            editTextPassword.setError("Password is required");

            editTextPassword.requestFocus();

        }

        if (email.isEmpty()){

            editTextEmail.setError("Email is required");

            editTextEmail.requestFocus();

        }
        if(uriProfileImage == null){
            editTextEmail.setError("Photo is required");
            photo.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            editTextEmail.setError("Please enter a valid email");

            editTextEmail.requestFocus();

            return;

        }



        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    usernameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.hasChild(username)){
                                usernameRef.child(username).setValue(email);
                                undateUserinfo(username, uriProfileImage,mAuth.getCurrentUser());
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Log.d("SIGN UP ========" , usernameRef.child(username).getKey());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Username already registered",Toast.LENGTH_SHORT).show();

                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"User email already registered",Toast.LENGTH_SHORT).show();

                    }

                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });

    }

    private void undateUserinfo(final String username, final Uri uriProfileImage, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference("user_photo/"+currentUser.getUid()+"jpg");
        final StorageReference imageFilepath = mStorage.child(currentUser.getUid());
        imageFilepath.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileupdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"User Registered Successfull",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            uriProfileImage = data.getData() ;
            photo.setImageURI(uriProfileImage);


        }
    }

    @Override

    public void onClick (View view){

        switch (view.getId()){

            case R.id.buttonSignUp:

                RegisterUser();

                break;



            case R.id.buttonCancel:

                startActivity(new Intent(this, LoginActivity.class));

        }

    }

}
