package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Fragment.ProfileFragment;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class profileEditActivity extends AppCompatActivity {
    private Button btn_save;
    private EditText userNameEditView;
    private TextView userEmailEditView;
    private TextView userIdEditView;
    private EditText userContactEditView;
    private ImageView userEditImage;
    private String UserName;
    private String UserContact;
    private DatabaseReference mDatabase;
    private Uri UserEditImage;
    final int GET_FROM_GALLERY = 2;
    private Uri selectedImage = null;
    DatabaseReference databaseUser;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userNameEditView = (EditText) findViewById(R.id.profileEditUserName);
        userEmailEditView = findViewById(R.id.profileEditUserEmail);
        userIdEditView = findViewById(R.id.profileEditUserID);
        userContactEditView = (EditText) findViewById(R.id.profileEditUserContact);
        userEditImage = (ImageView) findViewById(R.id.profileEditUserImage);

        btn_save = findViewById(R.id.btn_saveProfile);
        mAuth = FirebaseAuth.getInstance();


        databaseUser = FirebaseDatabase.getInstance().getReference("users");


        userEmailEditView.setText("CurrentEmail: " + user.getEmail());
        userIdEditView.setText("ID(unmodifiable): " + user.getUid());
        userNameEditView.setText(user.getDisplayName());
        userContactEditView.setText(user.getEmail());


        userEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opeangalley();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = userNameEditView.getText().toString();
                UserContact = userContactEditView.getText().toString();

                user.updateEmail(UserContact)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.print("it worked");
                                }
                            }
                        });

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(UserName)
                        .setPhotoUri(selectedImage)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.print("it worked");
                                }
                            }
                        });
                if (selectedImage != null){
                    FirebaseUser current_user = mAuth.getCurrentUser();
                    String username = mAuth.getCurrentUser().getDisplayName();
                    undateUserinfo(username, selectedImage,current_user);
                }
                finish();

            }
        });
    }

    private void opeangalley() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                userEditImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
                        Adduser(currentUser.getUid(),username,uri.toString(),currentUser.getEmail());
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

    private void Adduser(String uid, String username, String photo_uri, String email) {
        Users user = new Users(email,username,uid);
        user.setPhoto(photo_uri);
        databaseUser.child(uid).setValue(user);
    }
}

