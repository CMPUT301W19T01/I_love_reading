package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemViewActivity extends AppCompatActivity {

    private EditText EditTextBookName;
    private EditText EditTextAuthorName;
    private EditText EditTextDescription;

    private Button BorrowButton;
    private Button WatchListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        final Intent result = getIntent();
        EditTextBookName = findViewById(R.id.EditTextBookName);
        EditTextAuthorName = findViewById(R.id.EditTextBookDetail);
        EditTextDescription = findViewById(R.id.EditTextDescriptionContent);
        BorrowButton = findViewById(R.id.ButtonRentBook);
        BorrowButton = findViewById(R.id.ButtonWatchList);
        String BookName = result.getStringExtra("BookName");
        String AuthorName = result.getStringExtra("AuthorName");
        String Description = result.getStringExtra("Description");
        Boolean Edit = result.getBooleanExtra("edit",false);
        final Boolean Status = result.getBooleanExtra("status",false);
        EditTextBookName.setText(BookName);
        EditTextAuthorName.setText(AuthorName);
        EditTextDescription.setText(AuthorName);


        if (Status){
            BorrowButton.setText("Unavailable");
            BorrowButton.setEnabled(false);
        }

        if (!Edit){
            EditTextBookName.setEnabled(false);
            EditTextAuthorName.setEnabled(false);
            EditTextDescription.setEnabled(false);

        }
        else{
            EditTextBookName.setText(BookName);
            EditTextAuthorName.setText(AuthorName);
            EditTextDescription.setText(AuthorName);

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
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            Intent resultIntent= new Intent();
            resultIntent.putExtra("do","edit");
            String BookName = EditTextBookName.getText().toString();
            String AuthorName = EditTextAuthorName.getText().toString();
            String Description = EditTextDescription.getText().toString();

            resultIntent.putExtra("BookName",BookName);
            resultIntent.putExtra("AuthorName", AuthorName);
            resultIntent.putExtra("Description", Description);
            setResult(Activity.RESULT_OK,resultIntent);
            finish();

        }

        return super.onKeyDown(keyCode, event);
    }

}
