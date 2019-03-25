package com.example.libo.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Activity.profileEditActivity;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private Button btn_edit;
    private Button btn_refresh;
    public TextView userNameView;
    private TextView userEmailView;
    private TextView userId;
    private ImageView userImage;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_page, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userNameView = getActivity().findViewById(R.id.profileUserName);
        userEmailView = getActivity().findViewById(R.id.profileUserEmail);
        userId = getActivity().findViewById(R.id.profileEditUserID);
        userImage = getActivity().findViewById(R.id.profileUserImage);

        userNameView.setText("name: "+ user.getDisplayName());
        userEmailView.setText("email: "+user.getEmail());
        userId.setText("id: "+user.getUid());
        userImage.setImageURI(user.getPhotoUrl());



        btn_edit = getActivity().findViewById(R.id.btn_editProfile);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), profileEditActivity.class);
                startActivity(intent);
            }


        });

        btn_refresh = getActivity().findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                userNameView.setText("name: "+ user.getDisplayName());
                userEmailView.setText("email: " + user.getEmail());
                userImage.setImageURI(user.getPhotoUrl());
            }
        });


    }



}