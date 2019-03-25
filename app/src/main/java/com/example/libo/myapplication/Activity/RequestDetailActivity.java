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

import com.example.libo.myapplication.Model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

public class RequestDetailActivity extends AppCompatActivity {

    private Button accept;
    private Button deny;
    private Button scan;
    private Button map;
    private Request request;
    private int ScanResultCode = 4;
    private String ISBNCode;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        accept = findViewById(R.id.button_accept);
        deny = findViewById(R.id.button_deny);
        scan = findViewById(R.id.button_scan);
        map = findViewById(R.id.button_map);
        TextView ownerId = findViewById(R.id.request_owner_id);


        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("request");
        ownerId.setText(request.getReceiver());

        //If current user is owner and not accept request, show accept and deny button. Otherwise, hide them.
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(request.getReceiver()) && !request.isAccepted()){

            accept.setVisibility(View.VISIBLE);
            deny.setVisibility(View.VISIBLE);
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

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(RequestDetailActivity.this, CodeScanner.class);
                startActivityForResult(scanIntent, ScanResultCode);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If user is borrower, only show location
                latLng = request.getLatLng();
                double latitude = latLng.getLatitude();
                double longitude = latLng.getLongitude();
                Intent intent = new Intent(RequestDetailActivity.this, MapActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    public void accept(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        // when owner select accept, let owner choose geo immediately.
        Intent placePickerIntent;
        try {
            placePickerIntent = builder.build(RequestDetailActivity.this);
            startActivityForResult(placePickerIntent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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
        // when owner user select location on the map and pass latLng to previous activity
        if(requestCode == 1){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                Log.d("BYF", place.getAddress().toString());
                LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                Intent intent = new Intent();
                intent.putExtra("result", "accept");
                double latitude = latLng.getLatitude();
                double longitude = latLng.getLongitude();
                Log.d("byf", String.valueOf(latitude));
                Log.d("byf", String.valueOf(longitude));
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "Accept the request.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


}
