package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.app.ActionBar;


import com.example.libo.myapplication.R;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.getSupportActionBar().hide();
        setContentView(R.layout.activity_add_comment);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.add_comment_title_bar);
        getSupportActionBar().setElevation(2);
        final RatingBar ratingRatingBar = (RatingBar) findViewById(R.id.CreateCommentRatingBar);
        Button submitButton = (Button) findViewById(R.id.CommentConfirmButton);
        ImageButton CloseButton = (ImageButton) findViewById(R.id.CommentCloseButton);
        final TextView ratingDisplayTextView = (TextView) findViewById(R.id.EnterCommentTextView);
        Intent data = getIntent();
        Boolean update = data.getBooleanExtra("update",false); // If we want to update the comment instead of edit
        if (update){
            float rate = data.getFloatExtra("rate",0);
            String commentContent = data.getStringExtra("comment");
            ratingRatingBar.setRating(rate);
            ratingDisplayTextView.setText(commentContent);
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("close",false); //It's not closing comment
                resultIntent.putExtra("rate",ratingRatingBar.getRating());
                resultIntent.putExtra("Comment",ratingDisplayTextView.getText().toString());
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });

        CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("close", true);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });

    }


}
