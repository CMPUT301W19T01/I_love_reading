package com.example.libo.myapplication.Fragment;

import android.content.Intent;
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

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;

import java.util.ArrayList;

public class OwnFragment extends Fragment {


    private TextView userNameTextView;
    ListView own_book_lv;
    ArrayAdapter<Book> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.own_page,container,false);
        own_book_lv = (ListView)view.findViewById(R.id.own_book);
        ArrayList<Book> arrayOwnedbooks = new ArrayList<>();
        Book book1 = new Book("aaa","author1","001",true,"dscr1");
        Book book2 = new Book("bbb","author2","002",true,"dscr2");
        arrayOwnedbooks.add(0,book1);
        arrayOwnedbooks.add(1,book2);
        adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayOwnedbooks);
        own_book_lv.setAdapter(adapter);


        Button add_button = (Button) view.findViewById(R.id.AddButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), com.example.libo.myapplication.Activity.ItemViewActivity.class);
                startActivity(intent);
            }
        });
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