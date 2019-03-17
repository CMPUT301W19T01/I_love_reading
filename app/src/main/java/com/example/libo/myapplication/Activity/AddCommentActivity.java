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

import com.example.libo.myapplication.R;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_add_comment);
        final RatingBar ratingRatingBar = (RatingBar) findViewById(R.id.CreateCommentRatingBar);
        Button submitButton = (Button) findViewById(R.id.CommentConfirmButton);
        ImageButton CloseButton = (ImageButton) findViewById(R.id.CommentCloseButton);
        final TextView ratingDisplayTextView = (TextView) findViewById(R.id.EnterCommentTextView);


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
