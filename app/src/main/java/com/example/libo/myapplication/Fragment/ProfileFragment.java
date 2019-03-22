package com.example.libo.myapplication.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;

public class ProfileFragment extends Fragment {
    private Button btn_save;
    private EditText userNameView;
    private EditText userEmailView;
    private EditText userId;
    private EditText userLocation;
    private ImageView userImage;
    private Users users = new Users("b@gmail.com","00001");
    final int GET_FROM_GALLERY = 2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_page,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userNameView = getActivity().findViewById(R.id.profileUserName);
        userEmailView = getActivity().findViewById(R.id.profileUserEmail);
        userId = getActivity().findViewById(R.id.profileUserID);
        userImage = getActivity().findViewById(R.id.profileUserImage);
        userLocation = getActivity().findViewById(R.id.profileUserLocation);


        userNameView.setText(users.getUsername());
        userEmailView.setText(users.getEmail());
        userId.setText(users.getUid());
        userLocation.setText("Southgate");

        btn_save = getActivity().findViewById(R.id.btn_saveProfile);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.setUsername(userNameView.getText().toString());
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GET_FROM_GALLERY);
            }
        });


    }
}
