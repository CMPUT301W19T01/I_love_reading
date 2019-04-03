package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Adapter.bookListViewAdapter;
import com.example.libo.myapplication.BookStatus;
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
import java.util.Arrays;
import java.util.HashMap;

/**
 * The type Borrow fragment contain all books that are borrowed ,requested and accepted .
 */
public class BorrowFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final int BORROW_REQUEST_CODE = 0;
    private static final String TAG = "BorrowedBookDatabase";
    private Button button;
    private TextView userNameTextView;
    /**
     * The Borrow book lv.
     */
    ListView borrow_book_lv;
    /**
     * The Adapter of book.
     */
    ArrayAdapter<Book> adapter;
    ArrayList<Book> backupAllbooks = new ArrayList<>();

    ArrayList<Book> myBorrowedBooksArray = new ArrayList<>();

    private int current_index = 0;
    /**
     * The Show.
     */
    boolean show=false;
    private Book currentBook;
    /**
     * The Array of borrowbooks.
     */
    ArrayList<Book> arrayBorrowbooks = new ArrayList<>();
    private String Userid;
    private DatabaseReference borrowedRef;
    private DatabaseReference requestRef;
    private DatabaseReference acceptRef;
    private HashMap<String, ArrayList<Book>> dictBooks;

    private Spinner spinner;

    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.borrow_page,container,false);
        borrow_book_lv = (ListView)view.findViewById(R.id.borrow_book);
        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(Userid);
        adapter = new bookListViewAdapter(getContext().getApplicationContext(), arrayBorrowbooks);
        borrow_book_lv.setAdapter(adapter);
        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(Userid);
        requestRef = FirebaseDatabase.getInstance().getReference("requestBook").child(Userid);
        acceptRef = FirebaseDatabase.getInstance().getReference("acceptedBook").child(Userid);

        spinner = view.findViewById(R.id.borrowfilter);
        ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(getActivity().getApplication(),R.array.borrowedfilter,android.R.layout.simple_spinner_item);
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filteradapter);

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
                ItemView.putExtra("status", currentBook.getNew_status());
                ItemView.putExtra("edit",false);
                ItemView.putExtra("Description", currentBook.getDescription());
                ItemView.putExtra("ownerId", currentBook.getOwnerId());
                ArrayList<String> ClassificationArray = new ArrayList<String>(Arrays
                        .asList(currentBook.getClassification().split("/")));
                ItemView.putExtra("ClassificationArray", ClassificationArray);
                Uri bookcover = Uri.parse(currentBook.getBookcoverUri());
                ItemView.putExtra("BookCover", bookcover);
                current_index = i;
                ItemView.putExtra("ButtonCode", 0);
                startActivityForResult(ItemView, BORROW_REQUEST_CODE); // request code 0 means it is from borrow fragement
            }
        });

        dictBooks = new HashMap<>();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SearchView searchView = getActivity().findViewById(R.id.searchView2);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView)searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear query
                searchView.setIconified(true);
                searchView.setQuery("", false);
                //Collapse the action view
                searchView.onActionViewCollapsed();
                //Collapse the search widget
                arrayBorrowbooks.clear();
                arrayBorrowbooks.addAll(myBorrowedBooksArray);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()!=0){
                    Log.d(TAG, "I reached here" + newText);

                    adapter.getFilter().filter(newText);

                }
                return false;
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myBorrowedBooksArray.clear();
                DataSnapshot borrowedBooks = dataSnapshot.child("borrowedBooks").child(Userid);
                for (DataSnapshot myBook : borrowedBooks.getChildren()){
                    Book book = myBook.getValue(Book.class);
                    Log.d("byf", book.getBookName());
                    myBorrowedBooksArray.add(book);
                }

                ArrayList<Book> myRequestBooksArray = new ArrayList<>();
                DataSnapshot requestBooks = dataSnapshot.child("requestBook").child(Userid);
                for(DataSnapshot borrowedBook : requestBooks.getChildren()){
                    Book book = borrowedBook.getValue(Book.class);
                    Log.d("byf", book.getBookName());
                    myRequestBooksArray.add(book);
                }

                ArrayList<Book> myAcceptedBooksArray = new ArrayList<>();
                DataSnapshot acceptedBooks = dataSnapshot.child("acceptedBook").child(Userid);
                for(DataSnapshot borrowedBook : acceptedBooks.getChildren()){
                    Book book = borrowedBook.getValue(Book.class);
                    Log.d("byf", book.getBookName());
                    myAcceptedBooksArray.add(book);
                }

                dictBooks.put("Borrowing", myBorrowedBooksArray);
                dictBooks.put("Request", myRequestBooksArray);
                dictBooks.put("Accepted", myAcceptedBooksArray);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Object iitem = parent.getItemAtPosition(position);

                if(item.equals("Request")){
                    arrayBorrowbooks = dictBooks.get("Request");
                    Log.d("byf", String.valueOf(arrayBorrowbooks.size()));

                    adapter = new bookListViewAdapter(getContext(), arrayBorrowbooks);
                    borrow_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                if (item.equals("Accepted")){
                    arrayBorrowbooks = dictBooks.get("Accepted");
                    Log.d("byf", String.valueOf(arrayBorrowbooks.size()));

                    adapter = new bookListViewAdapter(getContext(), arrayBorrowbooks);
                    borrow_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                if (item.equals("Borrowing")) {
                    arrayBorrowbooks = dictBooks.get("Borrowing");
                    Log.d("byf", String.valueOf(arrayBorrowbooks.size()));

                    adapter = new bookListViewAdapter(getContext(), arrayBorrowbooks);
                    borrow_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*
        borrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowedRef.addValueEventListener(valueEventListener);
            }
        });


        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRef.addValueEventListener(valueEventListener);
            }
        });


        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRef.addValueEventListener(valueEventListener);
            }
        });*/

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

    /**
     * The Value event listener.
     */
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrayBorrowbooks.clear();
            backupAllbooks.clear();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Book book = ds.getValue(Book.class);
                arrayBorrowbooks.add(book);
                backupAllbooks.add(book);
            }
            adapter.notifyDataSetChanged();
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}