package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class bookListViewAdapter extends ArrayAdapter<Book> {

    private static final String TAG = "BookListAdapter";
    private Context bContext;
    int mResource;


    public bookListViewAdapter(Context context, int resource, ArrayList<Book> objects) {
        super(context, resource, objects);
        this.bContext = bContext;
        this.mResource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String bookName = getItem(position).getBookName();

        LayoutInflater inflater = LayoutInflater.from(bContext);
        convertView = inflater.inflate(R.layout.adapter_book_listview, parent,false);
        TextView bookNameV = (TextView)convertView.findViewById(R.id.BookListView1);

        bookNameV.setText(bookName);
        return convertView;

    }
}
