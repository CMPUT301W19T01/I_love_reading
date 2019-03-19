package com.example.libo.myapplication.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Fragment.AllFragment;
import com.example.libo.myapplication.Fragment.BorrowFragment;
import com.example.libo.myapplication.Fragment.OwnFragment;
import com.example.libo.myapplication.Fragment.ProfileFragment;
import com.example.libo.myapplication.Fragment.RequestFragment;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    private static final int CHOOSE_IMAGE = 101;

    private OwnFragment ownFragment;
    private ProfileFragment profileFragment;
    private BorrowFragment borrowFragment;
    private AllFragment allFragment;
    private RequestFragment requestFragment;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth mAuth;

    private String userID;

    private TextView Username;
    private ImageView UserPhoto;

    private Fragment[] fragments;
    private int lastFragment;
    private Uri uriprofileImage;
    private String ProfileImageUri;

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
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        Username = (TextView) findViewById(R.id.TextViewUserName);


        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Log.d(TAG,"USER TAG  datasnapshot   " + dataSnapshot.toString());
                Users current_user = new Users();
                //current_user.setUsername(dataSnapshot.child(userID).getValue(Users.class).getUsername());
                //Username.setText(current_user.getUsername());

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
