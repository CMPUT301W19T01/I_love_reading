package com.example.libo.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.libo.myapplication.R;

public class ProfileFragment extends Fragment {
    private Button button;
    private TextView userNameView;
    private TextView userEmailView;
    private TextView userLocationView;



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
        userLocationView = getActivity().findViewById(R.id.profileUserLocation);
        userNameView.setText("user_1");
        userEmailView.setText("b@gmail.com");
        userLocationView.setText("southgate");


    }
}
