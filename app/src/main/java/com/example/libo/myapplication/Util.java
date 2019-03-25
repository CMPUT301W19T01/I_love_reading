package com.example.libo.myapplication;

import com.example.libo.myapplication.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Util {
    private static DatabaseReference FirebaseRequests = FirebaseDatabase.getInstance().getReference("requests");

    public static void SendRequset(String bookOwner, String Bookid, Boolean isBorrowed) {

        Request request = new Request();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String sender =currentUser.getDisplayName();
        String sender_email = currentUser.getEmail();
        Date current_date = Calendar.getInstance().getTime();
        String senderId = currentUser.getUid();

        String requestid = FirebaseRequests.push().getKey();
        request.setDate(current_date);
        request.setReceiver(bookOwner);
        request.setSender(sender);
        request.setSenderEmail(sender_email);
        request.setSenderId(senderId);
        request.setBookId(Bookid);
        request.setBorrowed(isBorrowed);
        FirebaseRequests.child(bookOwner).child(requestid).setValue(request);
    }
}
