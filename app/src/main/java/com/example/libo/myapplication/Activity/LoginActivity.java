package com.example.libo.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.libo.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * The M auth.
     */
    FirebaseAuth mAuth;

    /**
     * The Edit text email.
     */
    EditText editTextEmail, /**
     * The Edit text password.
     */
    editTextPassword;

    /**
     * The Progress bar.
     */
    ProgressBar progressBar;

    private CheckBox mCheckBoxRemember;

    private static SharedPreferences mPrefs;

    private static final String PREFS_NAME="PrefsFile";



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.login_background);

        setContentView(R.layout.activity_login_gradbk);

        this.getSupportActionBar().hide();



        mAuth = FirebaseAuth.getInstance();
        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        findViewById(R.id.ButtonSignup).setOnClickListener(this);

        findViewById(R.id.ButtonLogin).setOnClickListener(this);
        findViewById(R.id.ButtonForgetPassward).setOnClickListener(this);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mCheckBoxRemember = (CheckBox) findViewById(R.id.CheckBoxRememberMe);
        getPreferencesData();

        Intent testIntent = getIntent();
        if (mPrefs.getString("pref_name","").length() != 0 && testIntent == null){
            SharedPreferences temp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String result_username = temp.getString("pref_name","not found");
            Intent intent = new Intent(LoginActivity.this, BasicActivity.class);
            intent.putExtra("username", result_username);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }
    private void getPreferencesData() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_name")) {
            String u = sp.getString("pref_name", "not found");
            editTextEmail.setText(u.toString());
        }
        if (sp.contains("pref_pass")) {
            String p = sp.getString("pref_pass", "not found");
            editTextPassword.setText(p.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }
    }


    /**
     * user Login
     */
    private void Userlogin(){

        String password = editTextPassword.getText().toString().trim();

        String email = editTextEmail.getText().toString().trim();



        if (password.isEmpty()){

            editTextPassword.setError("Password is required");

            editTextPassword.requestFocus();

        }

        if (email.isEmpty()){

            editTextEmail.setError("Email is required");

            editTextEmail.requestFocus();

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){


            editTextEmail.setError("Please enter a valid email");

            editTextEmail.requestFocus();

            return;

        }



        //progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    if (mCheckBoxRemember.isChecked()){
                        Boolean boolIsChecked = mCheckBoxRemember.isChecked();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref_name", editTextEmail.getText().toString());
                        editor.putString("pref_pass", editTextPassword.getText().toString());
                        editor.putBoolean("pref_check", boolIsChecked);
                        editor.apply();
                        Toast.makeText(getApplicationContext(),"Settings have been saved. ",Toast.LENGTH_SHORT).show();



                    }else{
                        mPrefs.edit().clear().apply();
                    }

                    Intent intent = new Intent(LoginActivity.this, BasicActivity.class);
                    intent.putExtra("username",editTextEmail.getText().toString());
                    intent.putExtra("request",false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);

                    //editTextEmail.getText().clear();
                    //editTextPassword.getText().clear();


                }else {

                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mPrefs.edit().clear().apply();

                }



            }


        });



    }





    @Override

    public void onClick (View view){

        switch (view.getId()){

            case R.id.ButtonSignup:

                startActivity(new Intent(this,SignUpActivity.class));

                break;



            case R.id.ButtonLogin:

                Userlogin();

                break;

            case R.id.ButtonForgetPassward:
                startActivity(new Intent(this,ResetPassward.class));
                break;


        }

    }

}