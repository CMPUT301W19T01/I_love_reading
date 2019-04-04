package com.example.libo.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Fragment.ProfileFragment;
import com.example.libo.myapplication.R;
import com.squareup.picasso.Picasso;

public class otherProfilePopupActivity extends AppCompatActivity {
    public TextView otherNameView;
    private TextView otherEmailView;
    private TextView otherId;
    private ImageView otherImage;
    private Button closeButton;
    private Intent result;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_user_profile);

        result = getIntent();
        otherId = findViewById(R.id.profilePopupUserId);
        otherNameView =findViewById(R.id.profilePopupUserName);
        otherEmailView = findViewById(R.id.profilePopupEmail);
        otherImage = findViewById(R.id.profilePopupImage);
        closeButton = findViewById(R.id.btnClose);


        otherEmailView.setText( result.getStringExtra("email"));
        otherId.setText(result.getStringExtra("uid"));
        otherNameView.setText(result.getStringExtra("name"));
        Picasso.with(this).load(result.getStringExtra("photo")).into(otherImage);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(otherProfilePopupActivity.this,BasicActivity.class));


            }


        });


    }


}
