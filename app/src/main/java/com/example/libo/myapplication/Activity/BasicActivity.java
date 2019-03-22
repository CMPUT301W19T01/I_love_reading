package com.example.libo.myapplication.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.libo.myapplication.Fragment.AllFragment;
import com.example.libo.myapplication.Fragment.BorrowFragment;
import com.example.libo.myapplication.Fragment.OwnFragment;
import com.example.libo.myapplication.Fragment.ProfileFragment;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Fragment.RequestFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";

    private OwnFragment ownFragment;
    private ProfileFragment profileFragment;
    private BorrowFragment borrowFragment;
    private AllFragment allFragment;
    private RequestFragment requestFragment;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private Fragment[] fragments;
    private int lastFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if (lastFragment != 0){
                        switchFragment(profileFragment, fragments[lastFragment]);
                        lastFragment = 0;
                    }
                    return true;
                case R.id.navigation_own:
                    if (lastFragment != 1){
                        switchFragment(ownFragment, fragments[lastFragment]);
                        lastFragment = 1;
                    }
                    return true;
                case R.id.navigation_borrow:
                    if (lastFragment != 2){
                        switchFragment(borrowFragment, fragments[lastFragment]);
                        lastFragment = 2;
                    }
                    return true;
                case R.id.navigation_all:
                    if (lastFragment != 3){
                        switchFragment(allFragment, fragments[lastFragment]);
                        lastFragment = 3;
                    }
                    return true;
                case R.id.navigation_request:
                    if (lastFragment != 4){
                        switchFragment(requestFragment, fragments[lastFragment]);
                        lastFragment = 4;
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        //username = (TextView) findViewById(R.id.Username);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Log.d(TAG,"Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Hide the action bar
        getSupportActionBar().hide();
        // Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initFragment();
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Users uInfo = new Users();
            /*
            uInfo.setUid(ds.child(userID).getValue(Users.class).getUid()); //set the name
            uInfo.setEmail(ds.child(userID).getValue(Users.class).getEmail()); //set the email
            uInfo.setUsername(ds.child(userID).getValue(Users.class).getUsername()); //set the phone_num
            Log.d(TAG,"Successfully signed in with: " + uInfo.getUsername());
            //username.setText(uInfo.getUsername());
            */

        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initFragment(){

        ownFragment = new OwnFragment();
        profileFragment = new ProfileFragment();
        borrowFragment = new BorrowFragment();
        allFragment = new AllFragment();
        requestFragment = new RequestFragment();

        fragments = new Fragment[]{profileFragment, ownFragment, borrowFragment, allFragment, requestFragment};
        lastFragment = 0;

        getSupportFragmentManager().beginTransaction().replace(R.id.basic_layout, profileFragment).show(profileFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void switchFragment(Fragment fragment, Fragment lastFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(lastFragment);
        if(!fragment.isAdded()){
            fragmentTransaction.add(R.id.basic_layout, fragment);
        }
        fragmentTransaction.show(fragment).commitAllowingStateLoss();
    }

}
