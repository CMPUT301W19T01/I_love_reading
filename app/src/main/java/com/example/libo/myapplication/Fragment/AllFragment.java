package com.example.libo.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.libo.myapplication.R;

import java.util.ArrayList;

public class AllFragment extends Fragment {

    private Button button;

    private TextView userNameTextView;

    ListView all_book_lv;

    ArrayAdapter<String> adapter;



    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.all_page,container,false);

        all_book_lv = (ListView)view.findViewById(R.id.all_book);

        ArrayList<String> arrayAllbooks = new ArrayList<>();

        arrayAllbooks.add("twew");

        arrayAllbooks.add("Bdsa");

        arrayAllbooks.add("dasS");



        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayAllbooks);

        all_book_lv.setAdapter(adapter);

        return view;

    }



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);



        SearchView searchView = getActivity().findViewById(R.id.searchView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override

            public boolean onQueryTextSubmit(String query) {

                return false;

            }



            @Override

            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;

            }

        });



    }

}