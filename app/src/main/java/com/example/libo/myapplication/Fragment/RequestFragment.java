package com.example.libo.myapplication.Fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Activity.ExampleService;
import com.example.libo.myapplication.Activity.RequestDetailActivity;
import com.example.libo.myapplication.Adapter.RequestAdapter;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The type Request fragment.
 */
public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "RequestDatabase";
    private TextView userNameTextView;
    private ListView requestList;
    private int currentIndex;
    private ArrayList<Request> requests;
    private RequestAdapter requestAdapter;
    private String userid;
    private Spinner spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.request_page,container,false);

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requests = new ArrayList<>();

        spinner = view.findViewById(R.id.request_filter);
        ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(getActivity().getApplication(),R.array.requestfilter,android.R.layout.simple_spinner_item);
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filteradapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestList = getActivity().findViewById(R.id.request_listview);

        requestAdapter = new RequestAdapter(getContext(), R.layout.request_cell, requests);
        requestList.setAdapter(requestAdapter);

        final SearchView searchView = getActivity().findViewById(R.id.request_search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                requestAdapter.getFilter().filter(newText);
                return false;
            }
        });

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

        //Let user decline the requests by clicking long on the request
        requestList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Request request = requests.get(i);
                AlertDialog.Builder a_builder = new AlertDialog.Builder(RequestFragment.this.getContext());
                a_builder.setMessage("Are you sure to delete this request?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requests.remove(request);
                                Util.FirebaseRequests.child(request.getReceiver()).child(request.getRequestId()).removeValue();
                                // make notification for user
                                Toast.makeText(RequestFragment.this.getContext(), "Delete the request successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = a_builder.create();
                alert.show(); // show the alert
                return true;
            }
        });
        final DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference("requests");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Object iitem = parent.getItemAtPosition(position);

                Toast.makeText(getContext(), iitem.toString(),
                        Toast.LENGTH_SHORT).show();

                if (item.equals("All")){
                    requestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requests.clear();
                            for(DataSnapshot owner : dataSnapshot.getChildren()){
                                for(DataSnapshot request : owner.getChildren()){
                                    Request requestClass = request.getValue(Request.class);
                                    if (requestClass.getSenderId().equals(userid) || requestClass.getReceiver().equals(userid)){

                                        requests.add(requestClass);
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                if (item.equals("My Request")){
                    requestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requests.clear();
                            for(DataSnapshot owner : dataSnapshot.getChildren()){
                                for(DataSnapshot request : owner.getChildren()){
                                    Request requestClass = request.getValue(Request.class);
                                    if (requestClass.getSenderId().equals(userid) ){
                                        requests.add(requestClass);
                                        if (requestClass.isAccepted()){
                                            //startService(requestClass);
                                            //Notification notification = new NotificationCompat.Builder(getContext(), "exampleServiceChannel")
                                            //       .setContentTitle("Example Service")
                                            //      .setContentText("test")
                                            //      .setSmallIcon(R.drawable.ic_launcher_foreground)
                                            //       //.setContentIntent(pendingIntent)
                                            //       .build();

                                        }
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                if(item.equals("Other Request")){
                    requestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requests.clear();
                            for(DataSnapshot owner : dataSnapshot.getChildren()){
                                for(DataSnapshot request : owner.getChildren()){
                                    Request requestClass = request.getValue(Request.class);
                                    if (requestClass.getReceiver().equals(userid) ){
                                        requests.add(requestClass);
                                        //startService(requestClass);
                                    }
                                }
                            }
                            requestAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Update request from database
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests.clear();
                for(DataSnapshot owner : dataSnapshot.getChildren()){
                    for(DataSnapshot request : owner.getChildren()){
                        Request requestClass = request.getValue(Request.class);
                        if (requestClass.getSenderId().equals(userid) || requestClass.getReceiver().equals(userid)){

                            requests.add(requestClass);
                            //startService(requestClass);

                        }
                    }
                }
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Start service.
     */
/*
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
                        uploadRequest(bookID, request);
                    }
                    if (data.getStringExtra("result").equals("deny")) {
                        requests.remove(currentIndex);
                        requestAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
    */
    public void startService(Request request) {

        Intent serviceIntent = new Intent(getActivity(), ExampleService.class);
        serviceIntent.putExtra("inputExtra", "New notification about: "+request.getBookName().toString());

        ContextCompat.startForegroundService(getActivity(), serviceIntent);
    }

    //public void stopService(View v) {
    //   Intent serviceIntent = new Intent(this, ExampleService.class);
    //    stopService(serviceIntent);
    //}
}