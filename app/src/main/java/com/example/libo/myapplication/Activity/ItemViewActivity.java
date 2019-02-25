package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.libo.myapplication.R;

public class ItemViewActivity extends AppCompatActivity {



    private TextView BookNameTextView;
    private TextView AuthorNameTextView;
    private TextView DescriptionTextView;
    private Button BorrowButton;
    private Button WatchListButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_view);

        final Intent result = getIntent();

        BookNameTextView = findViewById(R.id.EditTextBookName);

        AuthorNameTextView = findViewById(R.id.EditTextBookDetail);

        DescriptionTextView = findViewById(R.id.TextViewDescription);

        BorrowButton = findViewById(R.id.ButtonRentBook);

        BorrowButton = findViewById(R.id.ButtonWatchList);

        String BookName = result.getStringExtra("BookName");

        String AuthorName = result.getStringExtra("AuthorName");

        final Boolean Status = result.getBooleanExtra("status",false);

        BookNameTextView.setText(BookName);

        AuthorNameTextView.setText(AuthorName);


        if (Status){

            BorrowButton.setText("Unavailable");

        }


        BorrowButton.setOnClickListener(new View.OnClickListener() {

            @Override

            // Onclick listener for borrow button

            public void onClick(View v) {

                if (!Status){ //If available

                    Intent resultIntent = new Intent();

                    resultIntent.putExtra("do","borrow");

                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();

                }

            }

        });



        WatchListButton.setOnClickListener(new View.OnClickListener() {

            // Onclick listener for Watchlist button

            @Override

            public void onClick(View v) {

                Intent resultIntent= new Intent();

                resultIntent.putExtra("do","watchlist");

                setResult(Activity.RESULT_OK,resultIntent);

                finish();

            }

        });



    }

}
