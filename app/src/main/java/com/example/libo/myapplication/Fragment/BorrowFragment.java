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

public class BorrowFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final int BORROW_REQUEST_CODE = 0;
    private static final String TAG = "BorrowedBookDatabase";
    private Button button;
    private TextView userNameTextView;
    ListView borrow_book_lv;
    ArrayAdapter<Book> adapter;
    private int current_index = 0;
    boolean show=false;
    private Book currentBook;
    final ArrayList<Book> arrayBorrowbooks = new ArrayList<>();
    private String Userid;
    private DatabaseReference borrowedRef;
    private DatabaseReference requestRef;
    private DatabaseReference acceptRef;

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


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SearchView searchView = getActivity().findViewById(R.id.searchView2);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                Object iitem = parent.getItemAtPosition(position);



                if(item.equals("Request")){
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    requestRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            arrayBorrowbooks.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Book book = ds.getValue(Book.class);
                                BookStatus ssstatus = book.getNew_status();
                                if ((ssstatus.toString().equals("requested"))) {
                                    Log.d("byf===================", book.getID());
                                    Log.d(TAG, "====================current item is " + ssstatus);

                                    arrayBorrowbooks.add(book);
                                }


                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (item.equals("Accepted")){
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    acceptRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            arrayBorrowbooks.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Book book = ds.getValue(Book.class);
                                BookStatus ssstatus = book.getNew_status();
                                if ((ssstatus.toString().equals("accepted"))) {
                                    Log.d("byf===================", book.getID());

                                    arrayBorrowbooks.add(book);
                                }


                            }
                            adapter = new bookListViewAdapter(getContext().getApplicationContext(), arrayBorrowbooks);
                            //adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayOwnedbooks);

                            borrow_book_lv.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (item.equals("Borrowing")) {
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    borrowedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            arrayBorrowbooks.clear();

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Book book = ds.getValue(Book.class);
                                BookStatus ssstatus = book.getNew_status();
                                if ((ssstatus.toString().equals("borrowed"))) {
                                    Log.d("byf===================", book.getID());

                                    arrayBorrowbooks.add(book);
                                }


                            }
                            adapter = new bookListViewAdapter(getContext().getApplicationContext(), arrayBorrowbooks);
                            //adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayOwnedbooks);

                            borrow_book_lv.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrayBorrowbooks.clear();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Book book = ds.getValue(Book.class);
                arrayBorrowbooks.add(book);
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