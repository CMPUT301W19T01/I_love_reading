package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class bookListViewAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context myContext;


    public bookListViewAdapter(Context context, ArrayList<Book> books) {
        super(context, R.layout.adapter_book_listview, books);
        this.books = books;
        this.myContext = context;

    }

    private static class ViewHolder {
        TextView bookNameView;
        TextView authorNameView;
        TextView idView;
        ImageView bookCover;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String bookName = getItem(position).getBookName();
        String authorName = getItem(position).getAuthorName();
        String id = getItem(position).getOwnerName();
        String bookCover = getItem(position).getBookcoverUri() ;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.adapter_book_listview, parent,false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.bookNameView = (TextView)convertView.findViewById(R.id.BookListView1);
        viewHolder.authorNameView = (TextView)convertView.findViewById(R.id.BookListView2);
        viewHolder.idView = (TextView)convertView.findViewById(R.id.BookListView3);
        viewHolder.bookCover = (ImageView)convertView.findViewById(R.id.BookImageView1);

        viewHolder.bookNameView.setText("bookname: "+bookName);
        viewHolder.authorNameView.setText("author: "+authorName);
        viewHolder.idView .setText("owner: "+ id);
        Picasso.with(getContext()).load(bookCover).into(viewHolder.bookCover);



        return convertView;

    }

    public int getItemCount(){
        return books.size();
    }

    public Filter getFilter(){
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(books);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book item: books) {
                    if (item.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            books.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
