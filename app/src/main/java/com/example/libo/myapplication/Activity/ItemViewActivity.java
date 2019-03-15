package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Adapter.CommentAdapter;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ItemViewActivity extends AppCompatActivity {

    private EditText EditTextBookName;
    private EditText EditTextAuthorName;
    private EditText EditTextDescription;
    private TextView TextViewClassification;
    private ImageView ImageViewBookCover;
    private Button BorrowButton;
    private Button WatchListButton;
    private Button AddCommentButton;
    private Intent temp;
    private ListView ListViewComment;
    private String[] ItemSet = {"Science Fiction", "Philosophy", "Comedy", "Horror Fiction", "History"}; // Possible selection in classification
    private boolean[] SelectedItemSet;
    private ArrayList<Integer> myUserItems = new ArrayList<>(); //Selected items in terms of binary
    private ArrayList<String> resultClassification= new ArrayList<>(); //Selected items in terms of String
    private ArrayList<Comment> comments;
    private Intent resultIntent = new Intent();
    private CommentAdapter adapter;
    final int GET_FROM_GALLERY = 2;
    final int GET_FROM_COMMENT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent result = getIntent();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SelectedItemSet = new boolean[ItemSet.length];
        temp = result;
        setContentView(R.layout.activity_item_view);
        EditTextBookName = findViewById(R.id.EditTextBookName);
        EditTextAuthorName = findViewById(R.id.EditTextBookDetail);
        EditTextDescription = findViewById(R.id.EditTextDescriptionContent);
        TextViewClassification = findViewById(R.id.TextViewClassificationSelect);
        ImageViewBookCover = findViewById(R.id.ImageViewBookCover);
        BorrowButton = findViewById(R.id.ButtonRentBook);
        WatchListButton = findViewById(R.id.ButtonWatchList);
        AddCommentButton = findViewById(R.id.ButtonAddComment);
        ListViewComment = findViewById(R.id.ListViewComments);
        String BookName = result.getStringExtra("BookName"); // Get information from the Intent
        String AuthorName = result.getStringExtra("AuthorName");
        String Description = result.getStringExtra("Description");
        ArrayList<String> ClassificationArray = result.getStringArrayListExtra("ClassificationArray");
        Bitmap BookCover = (Bitmap) result.getParcelableExtra("BookCover"); // Get Book Cover in the format of bitmap
        comments = (ArrayList<Comment>) result.getSerializableExtra("CommentArray");
        Boolean Edit = result.getBooleanExtra("edit",false);
        final Boolean Status = result.getBooleanExtra("status",false);
        if (!Edit){ // If we are viewing the info instead of borrowing
            resultIntent.putExtra("borrow","false"); //default setting
            resultIntent.putExtra("watchlist", "false");
            checkStatus(Status); // check if the book can be borrowed
        }
        checkEdit(Edit, BookName, AuthorName, Description, ClassificationArray, BookCover);
        adapter = new CommentAdapter(this, comments);
        ListViewComment.setAdapter(adapter);
        setListViewHeightBasedOnChildren(ListViewComment);



        BorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // Onclick listener for borrow button
            public void onClick(View v) {
                if (!Status){ //If available
                    resultIntent.putExtra("borrow","true");
                    BorrowButton.setClickable(false);
                    Toast.makeText(getBaseContext(), R.string.BorrowToast,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        WatchListButton.setOnClickListener(new View.OnClickListener() {
            // Onclick listener for Watchlist button
            @Override
            public void onClick(View v) {
                resultIntent.putExtra("watchlist","true");
                WatchListButton.setClickable(false);
                Toast.makeText(getBaseContext(), R.string.WatchListToast,
                        Toast.LENGTH_LONG).show();
            }
        });

        ImageViewBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GET_FROM_GALLERY);
            }
        });


        TextViewClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ItemViewActivity.this);
                mBuilder.setTitle(R.string.SelectionTile);
                mBuilder.setMultiChoiceItems(ItemSet, SelectedItemSet, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    myUserItems.add(which); }
                                else {
                                    myUserItems.remove((Integer.valueOf(which))); } }
                        });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ArrayList<String> item = new ArrayList<>();
                        for (int i = 0; i < myUserItems.size(); i++) {
                            item.add(ItemSet[myUserItems.get(i)]); }
                        resultClassification = item;
                        TextViewClassification.setText(CombineStringList(item)); }
                });

                mBuilder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < SelectedItemSet.length; i++) {
                            SelectedItemSet[i] = false;
                            myUserItems.clear();
                            resultClassification = new ArrayList<String>();
                            TextViewClassification.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        AddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CommentIntent= new Intent(ItemViewActivity.this, AddCommentActivity.class);
                startActivityForResult(CommentIntent,GET_FROM_COMMENT);
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
        if (requestCode == GET_FROM_COMMENT && resultCode == Activity.RESULT_OK){
            Intent resultIntent = getIntent();
            Boolean resultCommand = resultIntent.getBooleanExtra("close",true);
            if (!resultCommand) {
                double Rate = resultIntent.getDoubleExtra("rate", '0');
                String CommentText = resultIntent.getStringExtra("Comment");
                String UserName = "........."; //To be done later
                String CommentTime;
                Calendar cal = Calendar.getInstance();
                Date time = cal.getTime();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String CurrentTime = timeFormat.format(time);
                String CurrentDate = dateFormat.format(time);
                CommentTime = CurrentDate + ' ' + CurrentTime;
                Comment newComment = new Comment(Rate, UserName, CommentTime, CommentText);
                comments.add(newComment);
                adapter.notifyDataSetChanged();
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

    public void checkEdit(Boolean Edit, String BookName, String AuthorName, String Description, ArrayList<String> ClassificationArray, Bitmap BookCover){
        // This function checks if the Book is editable
        // If it's editable, text view will be able to edit
        if (Edit){
            EditTextBookName.setEnabled(true);
            EditTextAuthorName.setEnabled(true);
            EditTextDescription.setEnabled(true);
            TextViewClassification.setClickable(true);
            BorrowButton.setVisibility(View.GONE);
            WatchListButton.setVisibility(View.GONE);
            AddCommentButton.setVisibility(View.GONE);
        }
        else{
            EditTextBookName.setCursorVisible(false);
            EditTextBookName.setFocusable(false);
            EditTextAuthorName.setCursorVisible(false);
            EditTextAuthorName.setFocusable(false);
            EditTextDescription.setCursorVisible(false);
            EditTextDescription.setFocusable(false);
            ImageViewBookCover.setEnabled(false);
            TextViewClassification.setEnabled(false);
        }
        EditTextBookName.setText(BookName);
        EditTextAuthorName.setText(AuthorName);
        EditTextDescription.setText(Description);
        TextViewClassification.setText(CombineStringList(ClassificationArray));
        if (BookCover != null){
            ImageViewBookCover.setImageBitmap(BookCover);
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
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                resultIntent.putExtra("do","edit");
                                ImageViewBookCover.buildDrawingCache(); // send the image back
                                Bitmap image= ImageViewBookCover.getDrawingCache();
                                resultIntent.putExtra("BookCover",image);
                                String BookName = EditTextBookName.getText().toString();
                                String AuthorName = EditTextAuthorName.getText().toString();
                                String Description = EditTextDescription.getText().toString();
                                resultIntent.putExtra("BookName",BookName);
                                resultIntent.putExtra("AuthorName", AuthorName);
                                resultIntent.putExtra("Description", Description);
                                resultIntent.putExtra("ClassificationArray", resultClassification);
                                setResult(Activity.RESULT_OK,resultIntent);
                                finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Stay",
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
                resultIntent.putExtra("do","test");
                resultIntent.putExtra("Comment",comments);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public String CombineStringList(ArrayList<String> my_list){
        // The function converts a string arraylist to a string
        String new_string = "";
        for (String temp:my_list){
            new_string = new_string + temp + "/";
        }
        if (new_string.length()>1){
            return new_string.substring(0,new_string.length()-1);
        }
        else
            return "";
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
