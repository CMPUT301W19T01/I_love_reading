package com.example.libo.myapplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.example.libo.myapplication.Fragment.ProfileFragment;
import com.example.libo.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userNameEditView = (EditText) findViewById(R.id.profileEditUserName);
        userEmailEditView = findViewById(R.id.profileEditUserEmail);
        userIdEditView = findViewById(R.id.profileEditUserID);
        userContactEditView = (EditText)findViewById(R.id.profileEditUserContact);
        userEditImage = (ImageView)findViewById(R.id.profileEditUserImage);

        btn_save = findViewById(R.id.btn_saveProfile);


        userEmailEditView.setText("CurrentEmail: "+ user.getEmail());
        userIdEditView.setText("ID(unmodifiable): " + user.getUid());
        userNameEditView.setText(user.getDisplayName());
        userContactEditView.setText(user.getEmail());
        userEditImage.setImageURI(user.getPhotoUrl());


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = userNameEditView.getText().toString();
                UserContact = userContactEditView.getText().toString();

                user.updateEmail(UserContact)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    System.out.print("it worked");
                                }
                            }
                        });

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(UserName)
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

            }
        });



    }
}
