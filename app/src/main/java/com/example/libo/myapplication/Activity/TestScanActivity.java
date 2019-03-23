package com.example.libo.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.libo.myapplication.R;



public class TestScanActivity extends AppCompatActivity{

    Button startButton;
    TextView resultTextView;
    Integer ScamResultCode = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        startButton = (Button) findViewById(R.id.TestActivityScanButton);
        resultTextView = (TextView) findViewById(R.id.TestScanTextView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(TestScanActivity.this, CodeScanner.class);
                startActivityForResult(scanIntent, ScamResultCode);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScamResultCode && resultCode == Activity.RESULT_OK){
            String resultText = data.getStringExtra("code"); // This is the result Text
            /*
            Do What every you want with the result


             */
            resultTextView.setText(resultText);
        }
    }

}
