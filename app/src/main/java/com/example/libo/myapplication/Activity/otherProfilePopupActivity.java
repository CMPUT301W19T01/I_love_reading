package com.example.libo.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.R;
import com.squareup.picasso.Picasso;

public class otherProfilePopupActivity extends AppCompatActivity {
    public TextView otherNameView;
    private TextView otherEmailView;
    private TextView otherId;
    private ImageView otherImage;
    private Intent result;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_user_profile);

        result = getIntent();

        otherEmailView.setText( result.getStringExtra("email"));
        otherId.setText(result.getStringExtra("uid"));
        otherNameView.setText(result.getStringExtra("name"));
        Picasso.with(this).load(result.getStringExtra("photo")).into(otherImage);


    }

}
