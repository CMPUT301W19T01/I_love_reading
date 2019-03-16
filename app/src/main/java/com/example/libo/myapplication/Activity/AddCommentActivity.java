package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.libo.myapplication.R;

public class AddCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        final RatingBar ratingRatingBar = (RatingBar) findViewById(R.id.CreateCommentRatingBar);
        Button submitButton = (Button) findViewById(R.id.CommentConfirmButton);
        Button CloseButton = (Button) findViewById(R.id.CommentCloseButton);
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
