package com.example.libo.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.libo.myapplication.Adapter.RequestAdapter;
import com.example.libo.myapplication.Model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RequestPopup {

    // Initialize everything

    private Context context;
    private String bookId;

    public RequestPopup(Context context, String bookId) {
        this.context = context;
        this.bookId = bookId;
    }

    /**
     * The method that shows the alert dialogue for commenting
     */
    public void showRequests() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View promptView = layoutInflater.inflate(R.layout.request_popup, null);

        ListView listView = promptView.findViewById(R.id.request_popup_list_view);

        final ArrayList<Request> requests = new ArrayList<>();

        final AlertDialog ad = new AlertDialog.Builder(context)
                .setView(promptView)
                .create();

        Util.FirebaseRequests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    for(DataSnapshot request : user.getChildren()){
                        if(request.child("bookId").getValue(String.class).equals(bookId)){
                            Request newRequest = request.getValue(Request.class);
                            requests.add(newRequest);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RequestAdapter requestAdapter = new RequestAdapter(context, R.layout.request_cell, requests);
        listView.setAdapter(requestAdapter);

        ad.show();
    }
}