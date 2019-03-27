package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;

import java.util.ArrayList;


public class bookListViewAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context myContext;


    public bookListViewAdapter(Context context, ArrayList<Book> books) {
        super(context, R.layout.adapter_book_listview,books);
        this.books = books;
        this.myContext = context;

    }

    private static class ViewHolder {
        TextView bookNameView;
        TextView authorNameView;
        TextView idView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String bookName = getItem(position).getBookName();
        String authorName = getItem(position).getAuthorName();
        String id = getItem(position).getID();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.adapter_book_listview, parent,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.bookNameView = (TextView)convertView.findViewById(R.id.BookListView1);
        viewHolder.authorNameView = (TextView)convertView.findViewById(R.id.BookListView2);
        viewHolder.idView = (TextView)convertView.findViewById(R.id.BookListView3);

        viewHolder.bookNameView.setText(bookName);
        return convertView;

    }
}
