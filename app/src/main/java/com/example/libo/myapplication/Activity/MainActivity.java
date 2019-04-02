package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.libo.myapplication.R;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;
    private String user_name;
    private VideoView backgroundVideoview;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().hide();

        backgroundVideoview=findViewById(R.id.backgroundvideoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.projecttt);
        backgroundVideoview.setVideoURI(uri);
        backgroundVideoview.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
                //homeIntent.putExtra("preference",SaveSharedPreference.getSharedPreferences(MainActivity.this));
                //SaveSharedPreference.setUserName(MainActivity.this,user_name);
                //Intent basicIntent = new Intent(MainActivity.this,BasicActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);



        stopService();;

    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }



}




