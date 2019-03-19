package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.libo.myapplication.Activity.RequestDetailActivity;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestFragment extends Fragment {

    private TextView userNameTextView;
    private ListView requestList;
    private int currentIndex;
    private ArrayList<Request> requests;
    private ArrayAdapter arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.request_page,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestList = getActivity().findViewById(R.id.request_listview);
        Request request1 = new Request("ybai5","123","b@gmail.com",true, Calendar.getInstance().getTime());
        Request request2 = new Request("ybai5","123","b@gmail.com",false, Calendar.getInstance().getTime());
        requests = new ArrayList<>();
        requests.add(request1);
        requests.add(request2);

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, requests);
        requestList.setAdapter(arrayAdapter);

        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Request request = requests.get(i);
                currentIndex = i;
                Intent intent = new Intent(getContext(), RequestDetailActivity.class);
                intent.putExtra("request", request);
                startActivityForResult(intent, 0);
            }
        });

        requestList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return true;
            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (0): {
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getStringExtra("result").equals("accept")) {
                        Request request = requests.get(currentIndex);
                        request.setAccepted(true);
                    }
                    if (data.getStringExtra("result").equals("deny")) {
                        requests.remove(currentIndex);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
