package com.example.libo.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.libo.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;

    @Override


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().hide();

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(homeIntent);
                    finish();

                }
            },SPLASH_TIME_OUT);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeIntent = new Intent(MainActivity.this, BasicActivity.class);
                    homeIntent.putExtra("username",SaveSharedPreference.getUserName(MainActivity.this)); //Pass user name to basic activity
                    startActivity(homeIntent);
                    finish();

                }
            },SPLASH_TIME_OUT);
        }




    }

    public static class SaveSharedPreference
    {
        static final String PREF_USER_NAME= "username";

        static SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static void setUserName(Context ctx, String userName)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USER_NAME, userName);
            editor.apply();
        }

        public static String getUserName(Context ctx)
        {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
        }
    }



}
