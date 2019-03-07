package com.example.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ItemViewActivity extends AppCompatActivity {

    private EditText EditTextBookName;
    private EditText EditTextAuthorName;
    private EditText EditTextDescription;
    private ImageView ImageViewBookCover;
    private Button BorrowButton;
    private Button WatchListButton;
    private Intent temp;

    final int GET_FROM_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent result = getIntent();
        temp = result;
        setContentView(R.layout.activity_item_view);
        EditTextBookName = findViewById(R.id.EditTextBookName);
        EditTextAuthorName = findViewById(R.id.EditTextBookDetail);
        EditTextDescription = findViewById(R.id.EditTextDescriptionContent);
        ImageViewBookCover = findViewById(R.id.ImageViewBookCover);
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
                    BorrowButton.setClickable(false);
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
                WatchListButton.setClickable(false);
            }
        });

        ImageViewBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GET_FROM_GALLERY);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageViewBookCover.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
                AlertDialog alertDialog = new AlertDialog.Builder(ItemViewActivity.this).create();
                alertDialog.setTitle("Note: ");
                alertDialog.setMessage("You are quitting the edit view, do you want to save?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Stay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Don't save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent resultIntent= new Intent();
                                resultIntent.putExtra("do","donotedit");
                                finish();
                            }
                        });

                alertDialog.show();

            }
            else{
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
