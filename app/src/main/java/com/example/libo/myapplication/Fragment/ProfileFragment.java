package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.libo.myapplication.Activity.BasicActivity;
import com.example.libo.myapplication.Activity.CodeScanner;
import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Activity.LoginActivity;
import com.example.libo.myapplication.Activity.profileEditActivity;
import com.example.libo.myapplication.BookStatus;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liferay.mobile.screens.context.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import static de.greenrobot.event.EventBus.TAG;

public class ProfileFragment extends Fragment {
    private Button btn_edit;
    private Button btn_refresh;
    public TextView userNameView;
    private TextView userEmailView;
    private TextView userId;
    private ImageView userImage;
    private Button ButtonLogOut;
    private  ImageButton scanButton;
    private TextView TextViewOwnBookNum;
    private TextView TextViewBorrowBookNum;
    private TextView TextViewCommentBookNum;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final DatabaseReference currentuser = FirebaseDatabase.getInstance().getReference("users");
    static private Users User_user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_page, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
        userNameView = getActivity().findViewById(R.id.profileUserName);
        userEmailView = getActivity().findViewById(R.id.profileUserEmail);
        btn_refresh = getActivity().findViewById(R.id.btn_refresh);
        userId = getActivity().findViewById(R.id.profileEditUserID);
        userImage = (ImageView) getActivity().findViewById(R.id.profileUserImage);
        ButtonLogOut = (Button) getActivity().findViewById(R.id.btn_logout);
        scanButton = (ImageButton) getActivity().findViewById(R.id.scan_button);
        TextViewBorrowBookNum = (TextView) getActivity().findViewById(R.id.TextViewBorrowBook);
        TextViewOwnBookNum = (TextView) getActivity().findViewById(R.id.TextViewOwnBook);
        TextViewCommentBookNum = (TextView) getActivity().findViewById(R.id.TextViewCommentNum);

        userNameView.setText("name: "+ user.getDisplayName());
        userEmailView.setText("email: "+user.getEmail());
        userId.setText("ID  "+ user.getUid());
        UpdateNum();

        if (user.getPhotoUrl() != null){
            Log.d("USER PFORILE" ,""+ user.getPhotoUrl().toString());
            Picasso.with(getContext()).load(user.getPhotoUrl()).into(userImage);
        }

        btn_edit = getActivity().findViewById(R.id.btn_editProfile);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), profileEditActivity.class);
                startActivity(intent);
            }


        });

        btn_refresh = getActivity().findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                userNameView.setText("name: "+ user.getDisplayName());
                userEmailView.setText("email: " + user.getEmail());
                Picasso.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(userImage);
            }
        });

        ButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getActivity().getApplication(), LoginActivity.class);
                newIntent.putExtra("test","test");
                startActivity(newIntent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(getActivity().getApplication(), CodeScanner.class);
                startActivityForResult(scanIntent,1);

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final DatabaseReference allBook = FirebaseDatabase.getInstance().getReference("books");
        final Context context = getActivity();
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final String ISBNCode = data.getStringExtra("code"); // This is the result Text
            allBook.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot user:dataSnapshot.getChildren()){
                        for(DataSnapshot book: user.getChildren()){
                            Book thisBook = book.getValue(Book.class);
                            if (ISBNCode.equals(thisBook.getID())){
                                Intent ItemView = new Intent(context, ItemViewActivity.class); // set the intent to start next activity
                                ItemView.putExtra("BookName", thisBook.getBookName()); // Put the info of the book to next activity
                                ItemView.putExtra("AuthorName", thisBook.getAuthorName());
                                ItemView.putExtra("ID", thisBook.getID());
                                ItemView.putExtra("status", thisBook.getStatus());
                                ItemView.putExtra("edit",false);
                                ItemView.putExtra("Description", thisBook.getDescription());
                                ArrayList<String> ClassificationArray = new ArrayList<String>(Arrays
                                        .asList(thisBook.getClassification().split("/")));
                                ItemView.putExtra("ClassificationArray", ClassificationArray);
                                Uri bookcover = Uri.parse(thisBook.getBookcoverUri());
                                ItemView.putExtra("BookCover", bookcover);

                                ItemView.putExtra("ButtonCode", 2);
                                startActivity(ItemView); // request code 0 means it is from borrow fragement
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

                public void UpdateNum(){

        final DatabaseReference currentuser = FirebaseDatabase.getInstance().getReference("users");

        currentuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Users temp = ds.getValue(Users.class);
                    if (temp.getUid().equals(user.getUid())){
                        User_user = temp;
                        TextViewCommentBookNum.setText(String.valueOf(User_user.getCommentnum()));
                        Log.d(TAG, "current"+ User_user.getCommentnum());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference storageRef = FirebaseDatabase.getInstance().getReference("books").child(user.getUid());
        storageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextViewOwnBookNum.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference storageRef2 = FirebaseDatabase.getInstance().getReference("acceptedBook").child(user.getUid());
        storageRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextViewBorrowBookNum.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void displayNotification(View view){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"notificaiton");
        builder.setSmallIcon(R.drawable.icon_email);
        builder.setContentTitle("Notificaiton");
        builder.setContentText("You have a new notification");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        notificationManagerCompat.notify(001,builder.build());

    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateNum();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                update();
            }
        }, 4000);   //5 seconds

    }

    public void update(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userNameView.setText("name: "+ user.getDisplayName());
        userEmailView.setText("email: " + user.getEmail());
        Picasso.with(getActivity().getApplicationContext()).load(user.getPhotoUrl()).into(userImage);
    }

}
