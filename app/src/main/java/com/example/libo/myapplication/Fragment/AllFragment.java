package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Adapter.bookListViewAdapter;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AllFragment extends Fragment {
    private static final String TAG = "AllBookDatabase";

    private DatabaseReference AlldatabaseBook;
    private DatabaseReference FirebaseRequests;
    private TextView userNameTextView;
    ListView all_book_lv;
    ArrayAdapter<Book> adapter;
    //bookListViewAdapter adapter;
    ArrayList<Book> arrayAllbooks = new ArrayList<>();
    private int current_index = 0;
    private Book currentBook;




    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.all_page,container,false);

        all_book_lv = (ListView)view.findViewById(R.id.all_book);
        all_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(), ItemViewActivity.class); // set the intent to start next activity
                currentBook = arrayAllbooks.get(i);
                ItemView.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", currentBook.getAuthorName());
                ItemView.putExtra("ID", currentBook.getID());
                ItemView.putExtra("status", currentBook.getStatus());
                ItemView.putExtra("edit",false);
                ItemView.putExtra("Description", currentBook.getDescription());
                ArrayList<String> ClassificationArray = new ArrayList<String>(Arrays
                        .asList(currentBook.getClassification().split("/")));
                ItemView.putExtra("ClassificationArray", ClassificationArray);
                Uri bookcover = Uri.parse(currentBook.getBookcoverUri());
                ItemView.putExtra("BookCover", bookcover);
                current_index = i;
                startActivityForResult(ItemView, 2); // request code 2 means we are updating info of a book
            }
        });
        adapter = new bookListViewAdapter(this.getContext().getApplicationContext(), arrayAllbooks);
        all_book_lv.setAdapter(adapter);
        return view;
    }



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        SearchView searchView = getActivity().findViewById(R.id.searchView2);

        AlldatabaseBook = FirebaseDatabase.getInstance().getReference("books");
        FirebaseRequests = FirebaseDatabase.getInstance().getReference("requests");

        //Query query = FirebaseDatabase.getInstance().getReference("books").orderByChild().


        AlldatabaseBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayAllbooks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot newds : ds.getChildren()) {
                        Book book = newds.getValue(Book.class);
                        arrayAllbooks.add(book);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (data.getStringExtra("borrow").equals("true")) {
                Log.d(TAG,"The current book des is " + currentBook.getDescription());

                this.currentBook.setStatus(true);
                Util.SendRequset(currentBook.getOwnerId(),currentBook, true);
            }
        }

        switch (requestCode) {
            case (0): { // In the case that we are looking for if the user is trying to borrow book
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    if (data.getStringExtra("borrow").equals("true")) {
                        this.currentBook.setStatus(true);
                        /* The book is now borrowed, update your information
                         */
                    }
                    if (data.getStringExtra("watchlist").equals("true")){
                        /* The Book is now added to watchlist, update your information
                         */
                    }
                }
            }
            case (1): {// we are looking for the new information that the user edited the book.
                if (resultCode == Activity.RESULT_OK) {
                    String order = data.getStringExtra("do");
                    if (order.equals("edit")) {
                        currentBook = new Book("", "", "", false,"", null,"");
                        currentBook.setBookName(data.getStringExtra("BookName"));
                        currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                        currentBook.setDescription(data.getStringExtra("Description"));
                        currentBook.setClassification(data.getStringExtra("ClassificationArray"));
                        currentBook.setBookCover((Bitmap) data.getParcelableExtra("BookCover"));
                        arrayAllbooks.add(currentBook);
                    }
                }
            }
            case (2): {// we are updating info of a book
                if (resultCode == Activity.RESULT_OK) {
                    String order = data.getStringExtra("do");
                    if (order.equals("edit")) {
                        currentBook = arrayAllbooks.get(current_index);
                        currentBook.setBookName(data.getStringExtra("BookName"));
                        currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                        currentBook.setDescription(data.getStringExtra("Description"));
                        currentBook.setClassification(data.getStringExtra("ClassificationArray"));
                        currentBook.setBookCover((Bitmap) data.getParcelableExtra("BookCover"));
                        currentBook.setAuthorName(order);
                    }
                }
            }
        }
    }




}