package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libo.myapplication.Activity.BasicActivity;
import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Activity.MainActivity;
import com.example.libo.myapplication.Adapter.bookListViewAdapter;
import com.example.libo.myapplication.BookStatus;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type All fragment contain all books from firebase database.
 */
public class AllFragment extends Fragment implements AdapterView.OnItemSelectedListener /*implements SearchView.OnQueryTextListener*/{
    private static final String TAG = "AllBookDatabase";
    private TextView userNameTextView;
    /**
     * The book lv contain all books from firebase database with different status.
     */
    ListView all_book_lv;
    /**
     * The Adapter.
     */
    ArrayAdapter<Book> adapter;
    /**
     * The Array allbooks.
     */
    ArrayList<Book> arrayAllbooks = new ArrayList<>();
    private Book currentBook;
    private int current_index = 0;
    private DatabaseReference AlldatabaseBook;
    private DatabaseReference requestbookRef;
    private DatabaseReference availablebook;
    /**
     * The Show.
     */
    boolean show=false;
    //bookListViewAdapter adapter;

    private String UID;
    //  RecyclerView resultsListView  ;
    // String prevQuery = "" ;
    //ArrayList<Book>  resultbook;
    //ArrayAdapter<Book> resultsAdapter;

    ArrayList<Book> backupAllbooks = new ArrayList<>();


    private static final int all_request_code = 0;
    private DatabaseReference availableRef;
    private DatabaseReference allRef;
    private DatabaseReference borrowedRef;

    private Spinner spinner;







    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.all_page,container,false);
        all_book_lv = (ListView)view.findViewById(R.id.all_book);
        allRef = FirebaseDatabase.getInstance().getReference("books");
        borrowedRef = FirebaseDatabase.getInstance().getReference("acceptedBook");

        AlldatabaseBook = FirebaseDatabase.getInstance().getReference("books");


        spinner = view.findViewById(R.id.allfilter);
        ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(getActivity().getApplication(),R.array.allfragmentfilter,android.R.layout.simple_spinner_item);
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filteradapter);




        /*
            Fix the error, please add the code here when clicking the search button
         */

        view.findViewById(R.id.all_book_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*   SearchView searchbooks = (SearchView)view.findViewById(R.id.all_book_search);
          searchbooks.setQueryHint("Search for a book");
         searchbooks.setIconifiedByDefault(false);
        searchbooks.setOnQueryTextListener(this);*/




        all_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(), ItemViewActivity.class); // set the intent to start next activity
                currentBook = arrayAllbooks.get(i);
                ItemView.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", currentBook.getAuthorName());
                ItemView.putExtra("ID", currentBook.getID());
                ItemView.putExtra("status", currentBook.getNew_status());
                ItemView.putExtra("edit",false);
                ItemView.putExtra("Description", currentBook.getDescription());
                ArrayList<String> ClassificationArray = new ArrayList<String>(Arrays
                        .asList(currentBook.getClassification().split("/")));
                ItemView.putExtra("ClassificationArray", ClassificationArray);
                Uri bookcover = Uri.parse(currentBook.getBookcoverUri());
                ItemView.putExtra("BookCover", bookcover);
                ItemView.putExtra("ownerId", currentBook.getOwnerId());
                current_index = i;
                if (currentBook.getOwnerId().equals(FirebaseAuth.getInstance().getUid())){
                    ItemView.putExtra("edit",true);
                }

                startActivityForResult(ItemView, 2); // request code 2 means we are updating info of a book
            }
        });
        adapter = new bookListViewAdapter(this.getContext().getApplicationContext(), arrayAllbooks);
        all_book_lv.setAdapter(adapter);


        return view;
    }
    /*  @Override
      public void onStart(){
        super.onStart();

      resultbook.clear();
     resultbook.addAll(arrayAllbooks);
     resultsAdapter = new bookListViewAdapter(this.getContext().getApplicationContext(),resultbook);
     resultsListView.setAdapter(adapter);


       }*/



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        AlldatabaseBook = FirebaseDatabase.getInstance().getReference("books");
        borrowedRef = FirebaseDatabase.getInstance().getReference("acceptedBook");

        requestbookRef = FirebaseDatabase.getInstance().getReference("requestBook");
        UID = FirebaseAuth.getInstance().getUid();
        AlldatabaseBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayAllbooks.clear();
                backupAllbooks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot newds : ds.getChildren()) {
                        Book book = newds.getValue(Book.class);
                        arrayAllbooks.add(book);
                        backupAllbooks.add(book);
                    }
                }

                adapter.notifyDataSetChanged();
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


                if (item.equals("All")){
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }


                    AlldatabaseBook.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            arrayAllbooks.clear();
                            backupAllbooks.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                for(DataSnapshot newds : ds.getChildren()) {
                                    Book book = newds.getValue(Book.class);
                                    arrayAllbooks.add(book);
                                    backupAllbooks.add(book);
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                if (item.equals("Borrowed")){
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
                            arrayAllbooks.clear();
                            backupAllbooks.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                for(DataSnapshot newds : ds.getChildren()) {
                                    Book book = newds.getValue(Book.class);
                                    BookStatus ssstatus = book.getNew_status();

                                    Log.d(TAG, "====================current item is " + ssstatus);
                                    if ((ssstatus.toString().equals("borrowed"))||(ssstatus.toString().equals("accepted"))){
                                        Log.d("byf===================",book.getID());

                                        arrayAllbooks.add(book);
                                        backupAllbooks.add(book);
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                if (item.equals("Available")){
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    AlldatabaseBook.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            arrayAllbooks.clear();
                            backupAllbooks.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                for(DataSnapshot newds : ds.getChildren()) {
                                    Book book = newds.getValue(Book.class);
                                    BookStatus ssstatus = book.getNew_status();

                                    Log.d(TAG, "====================current item is " + ssstatus);
                                    if ((ssstatus.toString().equals("available"))||(ssstatus.toString().equals("requested"))){
                                        Log.d("byf===================",book.getID());

                                        arrayAllbooks.add(book);
                                        backupAllbooks.add(book);
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
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
            Commented the code below since there are no SearchView with id searchView2 in the layout of all_page.
            If you want to use search view to implement some features of searching,
            please add a search view in the layout that named "all_page".
        */


         final SearchView searchView = getActivity().findViewById(R.id.all_book_search);
         searchView.setSubmitButtonEnabled(true);
         searchView.setQueryHint("Search Here");

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
                Log.d(TAG, "numbe is "+ arrayAllbooks.size());
                arrayAllbooks.clear();
                arrayAllbooks.addAll(backupAllbooks);
                adapter.notifyDataSetChanged();
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                   arrayAllbooks.clear();
                   arrayAllbooks.addAll((ArrayList<Book>) backupAllbooks.clone());
                   adapter.notifyDataSetChanged();
                return false;
                }
            });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean check = false;
                searchView.clearFocus();
                for (Book currentBook:arrayAllbooks){
                    if (currentBook.getDescription() == query|| currentBook.getAuthorName()==query){
                        check = true;
                    }
                }
                if(!check){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                }
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

    }




    /**
     *
     * @param requestCode to detect which model
     * @param resultCode to detect activity's status
     * @param data data from current
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"THIS IS CASE ============================"+data.getStringExtra("borrow"));

        if (resultCode == Activity.RESULT_OK && data.getStringExtra("borrow")!= null){
            if (data.getStringExtra("borrow").equals("true")) {
                Log.d(TAG,"The current book des is " + currentBook.getDescription());

                this.currentBook.setStatus(true);
                Util.SendRequset(currentBook.getOwnerId(),currentBook, true);
                requestbookRef.child(UID).child(currentBook.getID()).setValue(currentBook);
            }


        }

        switch (requestCode) {
            case (0): { // In the case that we are looking for if the user is trying to borrow book
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    if (data.getStringExtra("borrow").equals("true")) {
                        this.currentBook.setStatus(true);
                        this.currentBook.setStatus(true);
                        Util.SendRequset(currentBook.getOwnerId(),currentBook, true);
                        requestbookRef.child(UID).child(currentBook.getID()).setValue(currentBook);
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
                        Bitmap temp = (Bitmap) data.getParcelableExtra("BookCover");
                        String bookID = currentBook.getID();
                        Util.uploadFile(temp, currentBook.getID(), currentBook
                                ,FirebaseAuth.getInstance().getUid());
                    }
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /*ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            arrayAllbooks.clear();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                Book book = ds.getValue(Book.class);
                arrayAllbooks.add(book);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/


    /*   @Override
      public boolean onQueryTextSubmit(String query) {

        return false;
    }

     @Override
     public boolean onQueryTextChange(String newText) {
       String query = newText.toLowerCase();
      ArrayList<Book> tempList = new ArrayList<>();
     if (prevQuery.length()>query.length()){
       resultbook.clear();
     resultbook.addAll(arrayAllbooks);


          }
        for (int i=0; i<resultbook.size();i++){
          Book book = resultbook.get(i);
        String bookname = book.getBookName().toLowerCase();
      String bookdescription = book.getDescription().toLowerCase();
    if (bookname.contains(query)|| bookdescription.contains(query)){
      tempList.add(book);
     }


    }
     resultbook.clear();
    if (query.length()==0){
      resultbook.addAll(arrayAllbooks);
    }else {
      resultbook.addAll(tempList);
     }
    resultsAdapter.notifyDataSetChanged();
    prevQuery=query;





    return false;
     }*/

}