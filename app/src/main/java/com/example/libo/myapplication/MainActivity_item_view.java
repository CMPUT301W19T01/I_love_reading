package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button start_button;
    private Book test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_button = (Button) findViewById(R.id.button_item_view);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ItemView = new Intent(MainActivity.this, ItemViewActivity.class); // set the intent to start next activity
                ItemView.putExtra("BookName", test.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", test.getAuthorName());
                ItemView.putExtra("ID", test.getID());
                ItemView.putExtra("status", test.getStatus());
                startActivityForResult(ItemView, 0); // request code 0 means we are looking for if the user decide to borrow the book
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): { // In the case that we are looking for if the user is trying to borrow book
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    if (data.getStringExtra("do").equals("borrow")) {
                        this.test.setStatus(true);
                        /* The book is now borrowed, update your information



                         */
                    }
                    else{

                        /* The Book is now added to watchlist, update your information




                         */
                    }
                }
            }
        }
    }





    public void set_test(String BookName, String ID, String AuthorName, Boolean Status){
        // used to test for bugs
        this.test.setAuthorName(AuthorName);
        this.test.setBookName(BookName);
        this.test.setID(ID);
        this.test.setStatus(Status);
    }

}
