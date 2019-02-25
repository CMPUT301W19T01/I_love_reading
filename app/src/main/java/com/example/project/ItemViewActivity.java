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
    private Intent temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent result = getIntent();
        temp = result;
        setContentView(R.layout.activity_item_view);
        EditTextBookName = findViewById(R.id.EditTextBookName);
        EditTextAuthorName = findViewById(R.id.EditTextBookDetail);
        EditTextDescription = findViewById(R.id.EditTextDescriptionContent);
        BorrowButton = findViewById(R.id.ButtonRentBook);
        WatchListButton = findViewById(R.id.ButtonWatchList);
        String BookName = result.getStringExtra("BookName");
        String AuthorName = result.getStringExtra("AuthorName");
        String Description = result.getStringExtra("Description");
        Boolean Edit = result.getBooleanExtra("edit",false);
        final Boolean Status = result.getBooleanExtra("status",false);


        checkStatus(Status);
        checkEdit(Edit, BookName, AuthorName, Description);

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

    public void checkStatus(Boolean Status){
        // This function checks if the Book is available.
        // If the Book is not available, it Borrow Button will show Unavailable
        if (Status){
            BorrowButton.setText("Unavailable");
            BorrowButton.setEnabled(false);
        }
    }

    public void checkEdit(Boolean Edit, String BookName, String AuthorName, String Description){
        // This function checks if the Book is editable
        // If it's editable, text view will be able to edit
        if (Edit){
            EditTextBookName.setEnabled(true);
            EditTextAuthorName.setEnabled(true);
            EditTextDescription.setEnabled(true);
        }
        else{
            EditTextBookName.setEnabled(false);
            EditTextAuthorName.setEnabled(false);
            EditTextDescription.setEnabled(false);
            EditTextBookName.setText(BookName);
            EditTextAuthorName.setText(AuthorName);
            EditTextDescription.setText(Description);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        // When the return button is pressed. Automatically transfer the required information back
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            // do something on back.
            Boolean Edit = temp.getBooleanExtra("edit",false);
            if (Edit){
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
            else{
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
