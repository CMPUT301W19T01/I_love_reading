package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter{
    private int resourceId;
    private ArrayList<Request> requests;

    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Request> requests) {
        super(context, resource, requests);
        resourceId = resource;
        this.requests = requests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Request currentRequest = requests.get(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView bookName =  view.findViewById(R.id.request_cell_bookname);
        TextView date =  view.findViewById(R.id.request_cell_date);
        TextView status =  view.findViewById(R.id.request_cell_status);

        String newBookName = currentRequest.getBookName();
        if(newBookName.length()>15){
            newBookName = newBookName.substring(0, 15) + "...";
        }

        bookName.setText(newBookName);
        date.setText(currentRequest.getDate().toString());
        String currentstatus;
        if(currentRequest.isAccepted()){
            currentstatus = "Accepted";
        }else{
            currentstatus = "Not Accepted";
        }

        status.setText(currentstatus);
        return view;
    }

    @Override
    public int getCount() {
        return requests.size();
    }
}
