package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Adapter.bookListViewAdapter;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrowFragment extends Fragment {

    private static final int BORROW_REQUEST_CODE = 0;
    private static final String TAG = "BorrowedBookDatabase";
    private Button button;
    private TextView userNameTextView;
    ListView borrow_book_lv;
    ArrayAdapter<Book> adapter;
    private int current_index = 0;
    private Book currentBook;
    final ArrayList<Book> arrayBorrowbooks = new ArrayList<>();
    private String Userid;
    private DatabaseReference borrowedRef;


    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.borrow_page,container,false);
        borrow_book_lv = (ListView)view.findViewById(R.id.borrow_book);
        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(Userid);
        adapter = new bookListViewAdapter(getContext().getApplicationContext(), arrayBorrowbooks);
        //adapter = new ArrayAdapter<Book>(getContext(),android.R.layout.simple_list_item_1,arrayBorrowbooks);
        borrow_book_lv.setAdapter(adapter);

        borrow_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(), ItemViewActivity.class); // set the intent to start next activity
                currentBook = arrayBorrowbooks.get(i);
                //Uri imageUri = Book.getBookCover();
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)
                //ItemView.putExtra("BookCover", BookCover);
                ItemView.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", currentBook.getAuthorName());
                ItemView.putExtra("ID", currentBook.getID());
                ItemView.putExtra("status", currentBook.getStatus());
                ItemView.putExtra("edit",false);
                ItemView.putExtra("Description", currentBook.getDescription());
                ItemView.putExtra("ClassificationArray", currentBook.getClassification());
                ItemView.putExtra("BookCover", currentBook.getBookCover());
                current_index = i;
                ItemView.putExtra("ButtonCode", 0);
                startActivityForResult(ItemView, BORROW_REQUEST_CODE); // request code 0 means it is from borrow fragement
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

        borrowedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayBorrowbooks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Book book = ds.getValue(Book.class);
                    Log.d(TAG,"ALL Book name" + book.getBookName());
                    ArrayList<String> Classification = new ArrayList<String>();

                    book.setClassification(Classification);

                    Bitmap bitmap = Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_8888);
                    arrayBorrowbooks.add(book);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (BORROW_REQUEST_CODE): { // In the case that we are looking for if the user is trying to borrow book
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    /*
                    if (data.getStringExtra("borrow").equals("true")) {
                        this.currentBook.setStatus(true);
                        // The book is now borrowed, update your information

                    }
                    if (data.getStringExtra("watchlist").equals("true")){
                        // The Book is now added to watchlist, update your information

                    }
                    */

                    if (data.getBooleanExtra("return", false)){
                        Util.SendRequset(currentBook.getOwnerId(), currentBook,false);
                    }

                }
            }
        }
    }





}
