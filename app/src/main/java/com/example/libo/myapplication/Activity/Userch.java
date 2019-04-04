package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.libo.myapplication.Adapter.UserAdapter;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Userch extends Activity {

    private DatabaseReference userRef;
    private ListView all_user_lv;
    ArrayList<Users> arrayAllusers = new ArrayList<>();
    private Users current_user;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userch);
        all_user_lv = (ListView) findViewById(R.id.theList);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        adapter = new UserAdapter(this,arrayAllusers);
        all_user_lv.setAdapter(adapter);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayAllusers.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    Log.d("current user is ","mm"+user.toString());
                    arrayAllusers.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        all_user_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_user = arrayAllusers.get(position);
                


            }
        });

    }


}
