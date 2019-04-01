package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.BookStatus;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.example.libo.myapplication.Model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestDetailActivity extends AppCompatActivity {

    private DatabaseReference requestDatabseRef;
    private DatabaseReference acceptedRef;
    private DatabaseReference borrowedRef;
    private DatabaseReference requestdRef;
    private DatabaseReference AllbooksRef;

    private Button accept;
    private Button deny;
    private Button scan;
    private Button map;
    private Request request;
    private int ScanResultCode = 4;
    private String ISBNCode;
    private Book book;
    private Boolean isOwner = false;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        requestDatabseRef = FirebaseDatabase.getInstance().getReference("requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        requestdRef = FirebaseDatabase.getInstance().getReference("requestBook");
        acceptedRef = FirebaseDatabase.getInstance().getReference("acceptedBook");
        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBook");
        accept = findViewById(R.id.button_accept);
        deny = findViewById(R.id.button_deny);
        scan = findViewById(R.id.button_scan);
        map = findViewById(R.id.button_map);


        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("request");

        /*
            If current user is owner and not accept request, show accept and deny button. Otherwise, hide them.
            After accepting the request, the scan button will show.
         */
        TextView type = findViewById(R.id.request_type);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(request.getReceiver())){
            isOwner = true;

            if (request.isBorrowed()){
                type.setText("I want to borrow your book.");
            }
            else{
                type.setText("I want to return your book");
            }
            if (!request.isAccepted()){
                accept.setVisibility(View.VISIBLE);
                deny.setVisibility(View.VISIBLE);
                scan.setVisibility(View.INVISIBLE);
            }
        }else{
            if (request.isBorrowed()){
                type.setText("Your borrow request has been sent.");
            }
            else{
                type.setText("Your return request has been sent.");
            }
            if(request.isAccepted()) {
                scan.setVisibility(View.VISIBLE);
            } else {
                scan.setVisibility(View.INVISIBLE);
            }
        }



        //Show the basic information of request(book's owner username; description,date...)
        TextView sender = findViewById(R.id.request_sender);
        TextView date = findViewById(R.id.request_date);
        final TextView des = findViewById(R.id.request_des);
        final TextView bookNmae = findViewById(R.id.request_book_name);
        final TextView owner  =  findViewById(R.id.request_book_owner);

        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("books");
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

        //Get and set description and book name
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    Log.d("byf", user.getKey());
                    if(user.hasChild(request.getBookId())){
                        book = user.child(request.getBookId()).getValue(Book.class);
                        if((book.getOwnerConfirmed() && isOwner) || (book.getBorrowerConfirmed() && ! isOwner)){
                            scan.setVisibility(View.INVISIBLE);
                        }
                        des.setText(book.getDescription());
                        bookNmae.setText(book.getBookName());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get and set owner's username
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.getKey().equals(book.getOwnerId())){
                        Users users = user.getValue(Users.class);
                        owner.setText(users.getUsername());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sender.setText(request.getSender()+ " ("+request.getSenderEmail()+")");
        date.setText(request.getDate().toString());


        //When users click scan button
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(RequestDetailActivity.this, CodeScanner.class);
                startActivityForResult(scanIntent, ScanResultCode);
            }
        });

        //when users click map button
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user is borrower, only show location, if the location is not selected yet, show error message
                latLng = request.getLatLng();
                if(latLng == null){
                    Toast.makeText(RequestDetailActivity.this, "This book's owner has not picked the location.", Toast.LENGTH_SHORT).show();
                    return;
                }
                double latitude = latLng.getLatitude();
                double longitude = latLng.getLongitude();
                Intent intent = new Intent(RequestDetailActivity.this, MapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    public void accept(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        // when owner select accept, let owner choose geo immediately.
        Intent placePickerIntent;
        try {
            placePickerIntent = builder.build(RequestDetailActivity.this);
            startActivityForResult(placePickerIntent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void deny(View view){
        requestDatabseRef.child(request.getRequestId()).removeValue();
        requestdRef.child(request.getSenderId()).child(request.getBookId()).removeValue();
        Toast.makeText(this, "Deny the request.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmBookStatus(final BookStatus bookStatus){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(RequestDetailActivity.this);
        a_builder.setMessage("Are you sure to denote the book as borrowed?")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        book.setNew_status(bookStatus);
                        if(isOwner){
                            book.setOwnerConfirmed(true);
                        }
                        else{
                            book.setBorrowerConfirmed(true);
                        }
                        if(book.getBorrowerConfirmed() && book.getOwnerConfirmed()){
                            updateBorrowed(request.getSenderId(), book.getID(), request.getReceiver());
                        }

                        updateBook(book,request.getSenderId());
                        Toast.makeText(RequestDetailActivity.this, "Denoted the book as " + bookStatus + "!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanResultCode && resultCode == Activity.RESULT_OK){
            ISBNCode = data.getStringExtra("code"); // This is the result Text
            if(ISBNCode.equals(book.getID())){
                if(request.isBorrowed())
                    confirmBookStatus(BookStatus.borrowed);
                else
                    confirmBookStatus(BookStatus.available);


                /*
                if(isOwner){
                    if(request.isBorrowed())
                        confirmBookStatus(BookStatus.borrowed);
                    else
                        confirmBookStatus(BookStatus.available);

                } else {
                    if(request.isBorrowed())
                        confirmBookStatus(BookStatus.borrowed);
                    else
                        confirmBookStatus(BookStatus.available);
                }
                */
            }

        }
        // when owner user select location on the map and pass latLng to previous activity
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                Log.d("BYF", place.getAddress().toString());
                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                request.setAccepted(true);
                String borrowerId = request.getSenderId();
                String bookID = request.getBookId();
                request.setBorrowed(true);

                request.setLatLng(latLng);
                //updateBorrowed(borrowerId,bookID,request.getReceiver());
                uploadRequest(bookID, request);
                updaterequestBook(request.getSenderId(),request.getBookId());
                Toast.makeText(this, "Accept the request.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updaterequestBook(final String senderId, final String bookId) {
        requestdRef.child(senderId).child(bookId).removeValue();
        book.setNew_status(BookStatus.accepted);
        acceptedRef.child(senderId).child(bookId).setValue(book);

        //Delete all user have request the book and send them message
        requestdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot wds : ds.getChildren()){
                        Book ALL_book = wds.getValue(Book.class);
                        if (ALL_book.getID().equals(book.getID())) {
                            requestdRef.child(senderId).child(bookId).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //After owner and borrower confirm that the book has been exchanged successfully, update the firebase accept
    private void updateBorrowed(final String borrowerId, final String bookID, final String receiver) {
        borrowedRef.child(borrowerId).child(bookID).setValue(book);
        acceptedRef.child(borrowerId).child(bookID).removeValue();

    }

    //After accepting the request, update the borrow and all lists
    private void updateBook(final Book newBook, final String senderId) {
        AllbooksRef = FirebaseDatabase.getInstance().getReference("books");
        AllbooksRef.child(newBook.getOwnerId()).child(newBook.getID()).setValue(newBook);
        /*
        AllbooksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    for(DataSnapshot book : user.getChildren()){
                        Book currentBook = book.getValue(Book.class);
                        if (currentBook.getID().equals(newBook.getID())) {
                            AllbooksRef.child(currentBook.getOwnerId()).child(currentBook.getID()).setValue(newBook);
                        }
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }

    private void uploadRequest(final String bookID, final Request request) {
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests").child(request.getReceiver());
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Request requestTemp = ds.getValue(Request.class);

                    if (requestTemp.getBookId().equals(bookID)){
                        Log.d("byf", ds.getKey());
                        requestRef.child(ds.getKey()).setValue(request);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
