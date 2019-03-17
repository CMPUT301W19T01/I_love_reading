package com.example.libo.myapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;

public class RequestDetailActivity extends AppCompatActivity {

    private Button accept;
    private Button deny;
    private Request request;
    private Button map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        accept = findViewById(R.id.button_accept);
        deny = findViewById(R.id.button_deny);
        map = findViewById(R.id.button_map);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetailActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("request");

        if(request.isAccepted()){
            accept.setVisibility(View.GONE);
            deny.setVisibility(View.GONE);
        }


        TextView sender = findViewById(R.id.request_sender);
        TextView date = findViewById(R.id.request_date);
        TextView type = findViewById(R.id.request_type);

        sender.setText(request.getSender()+ " ("+request.getSenderEmail()+")");
        date.setText(request.getDate().toString());
        if (request.isBorrowed()){
            type.setText("I want to borrow your book.");
        }
        else{
            type.setText("I want to return your book");
        }

    }

    public void accept(View view){
        Intent intent = new Intent();
        intent.putExtra("result", "accept");
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Accept the request.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void deny(View view){
        Intent intent = new Intent();
        intent.putExtra("result", "deny");
        setResult(RESULT_OK, intent);
        Toast.makeText(this, "Deny the request.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
