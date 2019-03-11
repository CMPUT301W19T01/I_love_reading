package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Activity.MainActivity;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;

import java.util.ArrayList;

public class OwnFragment extends Fragment {


    private TextView userNameTextView;
    ListView own_book_lv;
    ArrayAdapter<Book> adapter;
    private ArrayList<Book> arrayOwnedbooks;
    private Book currentBook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.own_page,container,false);
        own_book_lv = (ListView)view.findViewById(R.id.own_book);
        arrayOwnedbooks = new ArrayList<>();
        Book book1 = new Book("aaa","author1","001",true,"dscr1", new ArrayList<String>());
        Book book2 = new Book("bbb","author2","002",true,"dscr2", new ArrayList<String>());
        arrayOwnedbooks.add(0,book1);
        arrayOwnedbooks.add(1,book2);
        adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayOwnedbooks);
        own_book_lv.setAdapter(adapter);


        Button add_button = (Button) view.findViewById(R.id.AddButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ItemViewActivity.class);
                ArrayList<String> Classification = new ArrayList<String>();
                currentBook = new Book("", "", "", false,"", Classification);
                intent.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                intent.putExtra("AuthorName", currentBook.getAuthorName());
                intent.putExtra("ID", currentBook.getID());
                intent.putExtra("status", currentBook.getStatus());
                intent.putExtra("edit",true);
                intent.putExtra("Description", currentBook.getDescription());
                intent.putExtra("BookCover", currentBook.getBookCover());
                intent.putExtra("ClassificationArray", currentBook.getClassification());
                intent.putExtra("CommentArray",currentBook.getComments());
                startActivityForResult(intent, 1); // request code 0 means we are allowing the user to edit the book
            }
        });

        own_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(), ItemViewActivity.class); // set the intent to start next activity
                Book currentBook = arrayOwnedbooks.get(i);
                ItemView.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", currentBook.getAuthorName());
                ItemView.putExtra("ID", currentBook.getID());
                ItemView.putExtra("status", currentBook.getStatus());
                ItemView.putExtra("edit",true);
                ItemView.putExtra("Description", currentBook.getDescription());
                ItemView.putExtra("ClassificationArray", currentBook.getClassification());
                ItemView.putExtra("BookCover", currentBook.getBookCover());
                ItemView.putExtra("CommentArray",currentBook.getComments());

                startActivityForResult(ItemView, 1); // request code 0 means we are looking for if the user decide to borrow the book
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                        currentBook = new Book("", "", "", false,"", null);
                        currentBook.setBookName(data.getStringExtra("BookName"));
                        currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                        currentBook.setDescription(data.getStringExtra("Description"));
                        currentBook.setClassification(data.getStringArrayListExtra("ClassificationArray"));
                        currentBook.setBookCover((Bitmap) data.getParcelableExtra("BookCover"));
                        currentBook.setAuthorName(order);
                        arrayOwnedbooks.add(currentBook);
                    }
                }
            }
        }
    }



}