package com.example.libo.myapplication.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.RequestPopup;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import com.example.libo.myapplication.Adapter.CommentAdapter;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static de.greenrobot.event.EventBus.TAG;

/**
 * The Item view activity.
 * This Activity shows the detail information of a book
 */
public class ItemViewActivity extends AppCompatActivity {

    private EditText EditTextBookName;
    private EditText EditTextAuthorName;
    private EditText EditTextDescription;
    private TextView TextViewClassification;
    private ImageView ImageViewBookCover;
    private CardView CardViewComment;
    private Button BorrowButton;
    private Button ReturnButton;
    private Button WatchListButton;
    private ImageButton AddCommentButton;
    private Intent temp;
    private ListView ListViewComment; // The list View of the comment
    private String[] ItemSet = {"Science Fiction", "Philosophy", "Comedy", "Horror Fiction", "History"}; // Possible selection in classification
    private boolean[] SelectedItemSet;
    private ArrayList<Integer> myUserItems = new ArrayList<>(); //Selected items in terms of binary
    private ArrayList<String> resultClassification= new ArrayList<>(); //Selected items in terms of String
    private ArrayList<Comment> comments;
    private Intent resultIntent = new Intent(); //Initialization of result Intent
    private CommentAdapter adapter; // Adapter for Comment list view
    private String BookId;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    private boolean test_counter_boolean = false;
    private boolean test_counter_boolean2 = false;
    private boolean temp_stop_update = false;
    boolean Edit;

    private Intent result;

    final int GET_FROM_GALLERY = 2; // result code for getting image from user gallery to set book cover
    final int GET_FROM_COMMENT = 3; // result code for getting new comment
    final int UPDATE_COMMENT = 4; // update the information in comment
    private int current_comment_position = -1;

    MenuItem toolBarAddButton;

    private DatabaseReference commentsRef;
    private Uri BookCoverUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.avoid_scale_background);
        result = getIntent();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SelectedItemSet = new boolean[ItemSet.length];
        temp = result;
        /*
        Design of Tool Bar
        */
        setContentView(R.layout.activity_item_view);
        getSupportActionBar().setTitle("Details View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        Get Information of the book from the intent
         */

        //Get the book iD
        BookId = result.getStringExtra("ID");
        ArrayList<String> ClassificationArray = result.getStringArrayListExtra("ClassificationArray");
        final Uri BookCover = (Uri) result.getParcelableExtra("BookCover"); // Get Book Cover in the format of bitmap
        Edit = result.getBooleanExtra("edit",false);
        final Boolean Status = result.getBooleanExtra("status",false);

        intializeVaraibles();
        initializeStatus();

        comments = new ArrayList<>(); // Initialization of comment array
        //Get Comments from Firebase
        commentsRef = FirebaseDatabase.getInstance().getReference("commentsTEST").child(BookId);
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                final String BookName = result.getStringExtra("BookName"); // Get information from the Intent
                if (BookName.length() != 0 && !temp_stop_update) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Comment comment = ds.getValue(Comment.class);
                        comments.add(comment);
                    }
                }

                adapter = new CommentAdapter(getApplicationContext(), comments);
                ListViewComment.setAdapter(adapter);
                setListViewHeightBasedOnChildren(ListViewComment);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Onclick listener for borrow button
        BorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Status){ //If available
                    if (BorrowButton.getText() == "Borrow") {
                        BorrowButton.setText("Requested");
                        resultIntent.putExtra("borrow", "true");
                        Toast.makeText(getBaseContext(), R.string.BorrowToast,
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        BorrowButton.setText("Borrow");
                        resultIntent.putExtra("borrow","false");
                        Toast.makeText(getBaseContext(), R.string.BorrowCancelToast,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Onclick listener for Watchlist button
        WatchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WatchListButton.getText() == "Watch List") {
                    WatchListButton.setText("Added");
                    resultIntent.putExtra("watchlist", "true");
                    Toast.makeText(getBaseContext(), R.string.WatchListToast,
                            Toast.LENGTH_LONG).show();
                }
                else{
                    WatchListButton.setText("Watch List");
                    resultIntent.putExtra("watchlist", "true");
                    Toast.makeText(getBaseContext(), R.string.WatchListCancelToast,
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        // OnClick listener for BookCover
        ImageViewBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery_intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery_intent, GET_FROM_GALLERY);
            }
        });

        // Onclick listener for selecting Classification of the Book
        TextViewClassification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classificationPopup(view);
            }
        });

        // Onclick Listener for Add comment button
        AddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CommentIntent= new Intent(ItemViewActivity.this, AddCommentActivity.class);
                startActivityForResult(CommentIntent,GET_FROM_COMMENT);
                ((Activity) ItemViewActivity.this).overridePendingTransition(R.layout.animate_slide_up_enter, R.layout.animate_slide_up_exit);
            }
        });

        ListViewComment.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Comment current_comment = (Comment) ListViewComment.getItemAtPosition(position);
                Boolean check = (username.equals(current_comment.getUsername()));
                if (!check){
                    Toast.makeText(ItemViewActivity.this, "Single Click on Your Comment to Edit", Toast.LENGTH_SHORT).show();
                }
                else{
                    update_comment_firebase();
                    Intent CommentUpdateIntent= new Intent(ItemViewActivity.this, AddCommentActivity.class);
                    CommentUpdateIntent.putExtra("update",true);
                    CommentUpdateIntent.putExtra("rate", current_comment.getRating());
                    CommentUpdateIntent.putExtra("comment", current_comment.getContent());
                    current_comment_position = position;
                    startActivityForResult(CommentUpdateIntent,UPDATE_COMMENT);
                    ((Activity) ItemViewActivity.this).overridePendingTransition(R.layout.animate_slide_up_enter, R.layout.animate_slide_up_exit);
                }
            }
        });

        ListViewComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                final Comment current_comment = (Comment) ListViewComment.getItemAtPosition(position);
                Boolean check = (username.equals(current_comment.getUsername()));
                if (!check){
                    Toast.makeText(ItemViewActivity.this, "Long Click on Your Comment to Delete", Toast.LENGTH_SHORT).show();
                }
                else{
                    update_comment_firebase();
                    AlertDialog alertDialog = new AlertDialog.Builder(ItemViewActivity.this).create();
                    alertDialog.setTitle("Note: ");
                    alertDialog.setMessage("Do you want to delete this comment?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    test_counter_boolean2 = true;
                                    comments.remove(current_comment);
                                    commentsRef.child(current_comment.getCommentId()).removeValue();
                                    setListViewHeightBasedOnChildren(ListViewComment);
                                    adapter.notifyDataSetChanged();
                                    usersRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Users currentUser = ds.getValue(Users.class);
                                                if (currentUser.getUsername().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()) && test_counter_boolean2){
                                                    currentUser.setCommentnum(currentUser.getCommentnum()-1);
                                                    usersRef.child(currentUser.getUid()).setValue(currentUser);
                                                    test_counter_boolean2 = false;
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    update_comment_firebase();
                    alertDialog.show();

                }
                return true;
            }
        });
    }


    // Get result from other activities
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            BookCoverUri = selectedImage;
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
            Intent resultIntent = data;
            Boolean resultCommand = resultIntent.getBooleanExtra("close",true);
            if (!resultCommand) {
                test_counter_boolean = true;
                float Rate = resultIntent.getFloatExtra("rate", '0');
                String CommentText = resultIntent.getStringExtra("Comment");
                String UserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); //To be done later
                String CommentTime;
                Calendar cal = Calendar.getInstance();
                Date time = cal.getTime();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String CurrentTime = timeFormat.format(time);
                String CurrentDate = dateFormat.format(time);
                CommentTime = CurrentDate + ' ' + CurrentTime;
                Comment newComment = new Comment(Rate, UserName, CommentTime, CommentText);
                String commentId =  commentsRef.push().getKey();
                newComment.setCommentId(commentId);
                newComment.setUser_photo(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                commentsRef.child(commentId).setValue(newComment);
                comments.add(newComment);
                adapter.notifyDataSetChanged();
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Users currentUser = ds.getValue(Users.class);
                            if (currentUser.getUsername().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()) && test_counter_boolean){
                                currentUser.setCommentnum(currentUser.getCommentnum()+1);
                                usersRef.child(currentUser.getUid()).setValue(currentUser);
                                test_counter_boolean = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                update_comment_firebase();
            }
        }
        if (requestCode == UPDATE_COMMENT && resultCode == Activity.RESULT_OK){
            Intent resultIntent = data;
            Boolean resultCommand = resultIntent.getBooleanExtra("close",true);
            if (!resultCommand) {
                float Rate = resultIntent.getFloatExtra("rate", '0');
                String CommentText = resultIntent.getStringExtra("Comment");
                String UserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); //To be done later
                String CommentTime;
                Calendar cal = Calendar.getInstance();
                Date time = cal.getTime();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String CurrentTime = timeFormat.format(time);
                String CurrentDate = dateFormat.format(time);
                CommentTime = CurrentDate + ' ' + CurrentTime;
                Comment temp_comment = comments.get(current_comment_position);
                temp_comment.setContent(CommentText);
                temp_comment.setRating(Rate);
                temp_comment.setTime(CommentTime);
                comments.set(current_comment_position,temp_comment);
                commentsRef.child(temp_comment.getCommentId()).setValue(temp_comment);
                update_comment_firebase();

            }
            // Wait for update comment
            //
            //
            //
        }
    }


    public void classificationPopup(View view){
        if (Edit){
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
        else{
            TextViewClassification.setClickable(false);
        }
    }

    public void intializeVaraibles(){
               /*
        Initialization of Text view and button.
         */
        EditTextBookName = findViewById(R.id.EditTextBookName);
        EditTextAuthorName = findViewById(R.id.EditTextBookDetail);
        EditTextDescription = findViewById(R.id.EditTextDescriptionContent);
        TextViewClassification = findViewById(R.id.TextViewClassificationSelect);
        ImageViewBookCover = findViewById(R.id.ImageViewBookCover);
        BorrowButton = findViewById(R.id.ButtonRentBook);
        ReturnButton = findViewById(R.id.button_return);
        WatchListButton = findViewById(R.id.ButtonWatchList);
        AddCommentButton = findViewById(R.id.ButtonAddComment);
        ListViewComment = findViewById(R.id.ListViewComments);
        CardViewComment = findViewById(R.id.card_view_adapter);
    }

    public void initializeStatus(){
        ArrayList<String> ClassificationArray = result.getStringArrayListExtra("ClassificationArray");
        final Uri BookCover = (Uri) result.getParcelableExtra("BookCover"); // Get Book Cover in the format of bitmap
        Edit = result.getBooleanExtra("edit",false);
        final Boolean Status = result.getBooleanExtra("status",false);
        if (!Edit){ // If we are viewing the info instead of borrowing
            resultIntent.putExtra("borrow","false"); //default setting
            resultIntent.putExtra("watchlist", "false");
            checkStatus(Status); // check if the book can be borrowed
        }

        BorrowButton.setText("Borrow");
        WatchListButton.setText("Watch List");
        final String BookName = result.getStringExtra("BookName"); // Get information from the Intent
        String AuthorName = result.getStringExtra("AuthorName");
        String Description = result.getStringExtra("Description");
        checkEdit(Edit, BookName, AuthorName, Description, ClassificationArray, BookCover); // show the information of the Book

        int buttonCode = result.getIntExtra("ButtonCode", -1);
        if( buttonCode == 0){
            BorrowButton.setVisibility(View.INVISIBLE);
            WatchListButton.setVisibility(View.INVISIBLE);
            ReturnButton.setVisibility(View.VISIBLE);

            ReturnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("return", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
        else if(buttonCode == 1){
            BorrowButton.setVisibility(View.INVISIBLE);
            WatchListButton.setVisibility(View.INVISIBLE);
            ReturnButton.setVisibility(View.VISIBLE);
            ReturnButton.setText("View Requests");

            ReturnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestPopup requestPopup = new RequestPopup(ItemViewActivity.this,BookId);
                    requestPopup.showRequests();
                }
            });
        }
    }
    /**
     * Check if the Book is available
     *
     * @param Status the status
     */
    public void checkStatus(Boolean Status){
        // This function checks if the Book is available.
        // If the Book is not available, it Borrow Button will show Unavailable
        if (Status){
            BorrowButton.setText("Unavailable");
            BorrowButton.setEnabled(false);
        }
    }

    /**
     * Display the information of the Book
     *
     * @param Edit                 if we can edit
     * @param BookName            the book name
     * @param AuthorName          the author name
     * @param Description         the description
     * @param ClassificationArray the classification array
     * @param BookCover           the book cover
     */
    public void checkEdit(Boolean Edit, String BookName, String AuthorName, String Description, ArrayList<String> ClassificationArray, Uri BookCover){
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
            TextViewClassification.setClickable(false);
            EditTextBookName.setBackgroundResource(R.drawable.edittext_trans_broader);
            EditTextAuthorName.setBackgroundResource(R.drawable.edittext_trans_broader);
            EditTextDescription.setBackgroundResource(R.drawable.edittext_trans_broader);
        }
        EditTextBookName.setText(BookName);
        EditTextAuthorName.setText(AuthorName);
        EditTextDescription.setText(Description);
        TextViewClassification.setText(CombineStringList(ClassificationArray));
        if (CombineStringList(ClassificationArray) == "" && !Edit){
            TextViewClassification.setText("None");
        }
        if (BookCover != null){
            Picasso.with(this).load(BookCover).into(ImageViewBookCover);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_view_menu, menu);
        toolBarAddButton = menu.findItem(R.id.toolbar_add_book);
        Intent temp_intent = getIntent();
        Boolean temp_edit = temp_intent.getBooleanExtra("edit",false);
        if (!temp_edit){
            toolBarAddButton.setVisible(false);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        // When the return button is pressed. Automatically transfer the required information back
        for (Comment item_comment : comments) {
            commentsRef.child(item_comment.getCommentId()).setValue(item_comment);
            adapter.notifyDataSetChanged();
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something on back.
            Boolean Edit = temp.getBooleanExtra("edit",false);
            if (Edit){
                AlertDialog alertDialog = new AlertDialog.Builder(ItemViewActivity.this).create();
                alertDialog.setTitle("Warning:");
                alertDialog.setMessage("You are quitting the edit view, do you want to save?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!check_finish()){
                                    dialog.dismiss();
                                }
                                else {
                                    resultIntent.putExtra("do", "edit");
                                    ImageViewBookCover.buildDrawingCache(); // send the image back
                                    Bitmap image = ImageViewBookCover.getDrawingCache();
                                    resultIntent.putExtra("BookCover", image);
                                    String BookName = EditTextBookName.getText().toString();
                                    String AuthorName = EditTextAuthorName.getText().toString();
                                    String Description = EditTextDescription.getText().toString();
                                    resultIntent.putExtra("BookName", BookName);
                                    resultIntent.putExtra("AuthorName", AuthorName);
                                    resultIntent.putExtra("Description", Description);
                                    resultIntent.putExtra("ClassificationArray", CombineStringList(resultClassification));

                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }
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
                                setResult(Activity.RESULT_OK,resultIntent);
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

    /**
     * Combine string list string.
     *
     * @param my_list the my list
     * @return the string
     */
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

    /**
     * Sets list view height based on children.
     *
     * @param listView the list view
     */
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            update_comment_firebase();
                // do something on back.
                Boolean Edit = temp.getBooleanExtra("edit", false);
                if (Edit) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ItemViewActivity.this).create();
                    alertDialog.setTitle("Note: ");
                    alertDialog.setMessage("You are quitting the edit view, do you want to save?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!check_finish()){
                                        dialog.dismiss();
                                    }
                                    else {
                                        resultIntent.putExtra("do", "edit");
                                        ImageViewBookCover.buildDrawingCache(); // send the image back
                                        Bitmap image = ImageViewBookCover.getDrawingCache();
                                        resultIntent.putExtra("BookCover", image);
                                        String BookName = EditTextBookName.getText().toString();
                                        String AuthorName = EditTextAuthorName.getText().toString();
                                        String Description = EditTextDescription.getText().toString();
                                        resultIntent.putExtra("BookName", BookName);
                                        resultIntent.putExtra("AuthorName", AuthorName);
                                        resultIntent.putExtra("Description", Description);
                                        resultIntent.putExtra("ClassificationArray", CombineStringList(resultClassification));

                                        setResult(Activity.RESULT_OK, resultIntent);
                                        finish();
                                    }
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
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("do", "donotedit");
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                }
                            });

                    alertDialog.show();
                } else {
                    resultIntent.putExtra("do", "test");
                    resultIntent.putExtra("Comment", comments);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
            if (item.getItemId() == R.id.toolbar_add_book){
                if (check_finish()){
                    resultIntent.putExtra("do", "edit");
                    ImageViewBookCover.buildDrawingCache(); // send the image back
                    Bitmap image = ImageViewBookCover.getDrawingCache();
                    resultIntent.putExtra("BookCover", image);
                    String BookName = EditTextBookName.getText().toString();
                    String AuthorName = EditTextAuthorName.getText().toString();
                    String Description = EditTextDescription.getText().toString();
                    resultIntent.putExtra("BookName", BookName);
                    resultIntent.putExtra("AuthorName", AuthorName);
                    resultIntent.putExtra("Description", Description);
                    resultIntent.putExtra("ClassificationArray", CombineStringList(resultClassification));
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
        }


        return super.onOptionsItemSelected(item);
    }

    public void update_comment_firebase(){
        for (Comment item_comment : comments) {
            commentsRef.child(item_comment.getCommentId()).setValue(item_comment);
            adapter.notifyDataSetChanged();
        }

    }

    public boolean check_finish(){
        boolean result = true;
        if (EditTextBookName.getText().length() == 0){
            result = false;
            EditTextBookName.setError("Book name is required");
        }
        if (EditTextAuthorName.getText().length() == 0)
        {
            result = false;
            EditTextAuthorName.setError("Book name is required");
        }
        if (EditTextDescription.getText().length() == 0)
        {
            result = false;
            EditTextDescription.setError("Book name is required");
        }
        return result;
    }

}
