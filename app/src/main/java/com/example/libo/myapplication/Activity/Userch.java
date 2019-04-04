package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.libo.myapplication.Adapter.UserAdapter;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.liferay.mobile.screens.context.LiferayScreensContext.getContext;
import static de.greenrobot.event.EventBus.TAG;

public class Userch extends AppCompatActivity {

    private DatabaseReference userRef;
    private ListView all_user_lv;
    ArrayList<Users> arrayAllusers = new ArrayList<>();
    private Users current_user;
    private UserAdapter adapter;
    ArrayList<Users> backupAllusers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userch);
        getSupportActionBar().setTitle("User Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        all_user_lv = (ListView) findViewById(R.id.theList);
        userRef = FirebaseDatabase.getInstance().getReference("users");
        adapter = new UserAdapter(this,arrayAllusers);
        all_user_lv.setAdapter(adapter);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayAllusers.clear();
                backupAllusers.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    Log.d("current user is ","mm"+user.toString());
                    arrayAllusers.add(user);
                    backupAllusers.add(user);
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
                Intent other_profile = new Intent(getApplication(),otherProfilePopupActivity.class);
                other_profile.putExtra("name",current_user.getUsername());
                other_profile.putExtra("uid",current_user.getUid());
                other_profile.putExtra("email",current_user.getEmail());
                other_profile.putExtra("photo",current_user.getPhoto());
                startActivity(other_profile);
            }
        });

        final SearchView searchView = findViewById(R.id.user_search);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView)searchView.findViewById(searchCloseButtonId);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear query
                searchView.setIconified(true);
                searchView.setQuery("", false);
                //Collapse the action view
                searchView.onActionViewCollapsed();
                //Collapse the search widget
                Log.d(TAG, "numbe is "+ arrayAllusers.size());
                arrayAllusers.clear();
                arrayAllusers.addAll(backupAllusers);
                adapter.notifyDataSetChanged();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                arrayAllusers.clear();
                arrayAllusers.addAll((ArrayList<Users>) backupAllusers.clone());
                adapter.notifyDataSetChanged();
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean check = false;
                searchView.clearFocus();
                for (Users currentBook:arrayAllusers){
                    if (currentBook.getUsername() == query|| currentBook.getUid()==query){
                        check = true;
                    }
                }
                if(!check){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()!=0){
                    Log.d(TAG, "I reached here" + newText);
                    adapter.getFilter().filter(newText);

                }
                return false;
            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
