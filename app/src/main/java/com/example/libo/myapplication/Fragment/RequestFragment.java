package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.libo.myapplication.Activity.RequestDetailActivity;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequestFragment extends Fragment {
    private static final String TAG = "RequestDatabase";
    private TextView userNameTextView;
    private ListView requestList;
    private int currentIndex;
    private ArrayList<Request> requests;
    private ArrayAdapter arrayAdapter;

    private DatabaseReference requestDatabseRef;
    private DatabaseReference borrowedRef;
    private DatabaseReference AllbooksRef;
    private String userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.request_page,container,false);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requestDatabseRef = FirebaseDatabase.getInstance().getReference("requests").child(userid);
        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks");
        requests = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestList = getActivity().findViewById(R.id.request_listview);

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, requests);
        requestList.setAdapter(arrayAdapter);

        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Request request = requests.get(i);
                currentIndex = i;
                Intent intent = new Intent(getContext(), RequestDetailActivity.class);
                intent.putExtra("request", request);
                startActivityForResult(intent, 0);
            }
        });

        requestList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }


        });

        requestDatabseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,"The current news is   " + dataSnapshot.toString());
                for(DataSnapshot newds : dataSnapshot.getChildren()) {
                    Log.d(TAG,"The current news is   " + newds.toString());
                    Request request = newds.getValue(Request.class);
                    requests.add(request);
                }
                arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, requests);
                requestList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getStringExtra("result").equals("accept")) {
                        Request request = requests.get(currentIndex);
                        request.setAccepted(true);
                        String borrowerId = request.getSenderId();
                        String bookID = request.getBookId();
                        request.setBorrowed(true);
                        double lat = data.getDoubleExtra("latitude", 999);
                        double lng = data.getDoubleExtra("longitude", 999);
                        Log.d("byf", String.valueOf(lat));
                        Log.d("byf", String.valueOf(lng));
                        LatLng latLng = new LatLng(lat, lng);
                        request.setLatLng(latLng);
                        uploadBorrowed(borrowerId,bookID,request.getReceiver());
                    }
                    if (data.getStringExtra("result").equals("deny")) {
                        requests.remove(currentIndex);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void uploadBorrowed(final String borrowerId, final String bookID, final String receiver) {
        AllbooksRef = FirebaseDatabase.getInstance().getReference("books").child(receiver);
        AllbooksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Book book = ds.getValue(Book.class);
                    Log.d(TAG,"borrwed ====================" + book.getBookName() + bookID);

                    if (book.getID().equals(bookID)){
                        Log.d(TAG,"borrwed ====================" + book.getBookName());
                        borrowedRef.child(borrowerId).child(bookID).setValue(book);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
    }
}
