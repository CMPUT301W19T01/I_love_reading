package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

public class RequestDetailActivity extends AppCompatActivity {

    private Button accept;
    private Button deny;
    private Button scan;
    private Button map;

    private Request request;
    private int ScanResultCode = 4;
    private String ISBNCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        accept = findViewById(R.id.button_accept);
        deny = findViewById(R.id.button_deny);
        scan = findViewById(R.id.button_scan);
        map = findViewById(R.id.button_map);

        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("request");

        //If the requirement is accepted, hide the choose button
        if(request.isAccepted()){
            accept.setVisibility(View.GONE);
            deny.setVisibility(View.GONE);
        }

        //if owner choose map to select the location
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(RequestDetailActivity.this, MapsActivity.class);
                startActivity(intent);
                */
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent;
                try {
                    intent = builder.build(RequestDetailActivity.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

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

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(RequestDetailActivity.this, CodeScanner.class);
                startActivityForResult(scanIntent, ScanResultCode);
            }
        });

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


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanResultCode && resultCode == Activity.RESULT_OK){
            ISBNCode = data.getStringExtra("code"); // This is the result Text
            /*
            Do Whatever you want with the result


             */
        }
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                Log.d("BYF", place.getAddress().toString());
                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        }
    }


}
