package com.example.libo.myapplication.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.libo.myapplication.R;
import com.example.libo.myapplication.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The type Own fragment.
 */
public class OwnFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "BorrowedBookDatabase";
    private TextView userNameTextView;
    /**
     * The Own book lv contain books belong to current user.
     */
    ListView own_book_lv;
    /**
     * The Adapter of book.
     */
    ArrayAdapter<Book> adapter;
    private HashMap<String, ArrayList<Book>> dictBooks;
    private ArrayList<Book> arrayOwnedbooks;
    private Book currentBook;
    private int current_index = 0;
    ArrayList<Book> backupAllbooks = new ArrayList<>();
    ArrayList<Book> myBooksArray = new ArrayList<>();

    private DatabaseReference databaseBook;
    private StorageReference storageRef;

    private Spinner spinner;
    /**
     * The Show.
     */
    boolean show=false;


    private FirebaseAuth mAuth;

    private  String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        storageRef = FirebaseStorage.getInstance().getReference("bookcover");
        databaseBook = FirebaseDatabase.getInstance().getReference("books").child(userID);

        View view=inflater.inflate(R.layout.own_page,container,false);
        own_book_lv = (ListView)view.findViewById(R.id.own_book);
        dictBooks = new HashMap<>();
        ImageButton add_button = (ImageButton) view.findViewById(R.id.AddButton);

        spinner = view.findViewById(R.id.ownfilter);
        ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(getActivity().getApplication(),R.array.ownfilter,android.R.layout.simple_spinner_item);
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(filteradapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ItemViewActivity.class);

                currentBook = new Book("", "", "", false,"", "","");
                intent.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                intent.putExtra("AuthorName", currentBook.getAuthorName());
                intent.putExtra("ID", currentBook.getID());
                intent.putExtra("status", currentBook.getNew_status());
                intent.putExtra("edit",true);
                intent.putExtra("Description", currentBook.getDescription());
                intent.putExtra("BookCover", currentBook.getBookCover());
                ArrayList<String> ClassificationArray = new ArrayList<>();
                intent.putExtra("ClassificationArray", ClassificationArray);

                startActivityForResult(intent, 1); // request code 1 means we are allowing the user to edit the book

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
                ItemView.putExtra("status", currentBook.getNew_status());
                ItemView.putExtra("edit",true);
                ItemView.putExtra("Description", currentBook.getDescription());
                ItemView.putExtra("ownerId", currentBook.getOwnerId());
                ArrayList<String> ClassificationArray = new ArrayList<String>(Arrays
                        .asList(currentBook.getClassification().split("/")));
                ItemView.putExtra("ClassificationArray", ClassificationArray);
                Uri bookcover = Uri.parse(currentBook.getBookcoverUri());
                ItemView.putExtra("BookCover", bookcover);
                current_index = i;
                //ButtonCode 1 means user can view requests on one book by clicking request button
                ItemView.putExtra("ButtonCode",1);
                startActivityForResult(ItemView, 2); // request code 2 means we are updating info of a book
            }

        });

        own_book_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Book");
                builder.setMessage("Want to Delete the book ?");
                builder.setNeutralButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Book currentBook = arrayOwnedbooks.get(position);
                        databaseBook.child(currentBook.getID()).removeValue();
                        dialog.dismiss();
                        arrayOwnedbooks.remove(currentBook);
                        adapter.notifyDataSetChanged();
                    }

                });
                builder.show();
                return false;
            }
        });
        return view;
    }



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        final SearchView searchView = getActivity().findViewById(R.id.own_book_search);
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
                arrayOwnedbooks.clear();
                arrayOwnedbooks.addAll(myBooksArray);
                Log.d(TAG, "numbe is "+ arrayOwnedbooks.size());
                adapter.notifyDataSetChanged();
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
                myBooksArray.clear();
                DataSnapshot myBooks = dataSnapshot.child("books").child(userID);
                for (DataSnapshot myBook : myBooks.getChildren()){
                    Book book = myBook.getValue(Book.class);
                    Log.d("byf", book.getBookName());
                    myBooksArray.add(book);
                }

                ArrayList<Book> myBorrowedBooksArray = new ArrayList<>();
                DataSnapshot borrowedBooks = dataSnapshot.child("borrowedBook").child(userID);
                for(DataSnapshot borrowedBook : borrowedBooks.getChildren()){
                    Book book = borrowedBook.getValue(Book.class);
                    Log.d("byf", book.getBookName());
                    myBorrowedBooksArray.add(book);
                }

                ArrayList<Book> allBooks = new ArrayList<>();
                allBooks.addAll(myBooksArray);
                for(Book book : myBorrowedBooksArray){
                    if(!allBooks.contains(book))
                        allBooks.add(book);
                }

                ArrayList<Book> availableBooks = new ArrayList<>();
                for (Book book : myBooksArray){
                    if(book.getNew_status().equals(BookStatus.available)){
                        availableBooks.add(book);
                    }
                }

                dictBooks.put("All", allBooks);
                dictBooks.put("Available", availableBooks);
                dictBooks.put("Borrowed", myBorrowedBooksArray);
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
                    arrayOwnedbooks = dictBooks.get("All");
                    Log.d("byf", String.valueOf(arrayOwnedbooks.size()));

                    adapter = new bookListViewAdapter(getContext(), arrayOwnedbooks);
                    own_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                if(item.equals("Available")){
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    arrayOwnedbooks = dictBooks.get("Available");
                    Log.d("byf", String.valueOf(arrayOwnedbooks.size()));
                    adapter = new bookListViewAdapter(getContext(), arrayOwnedbooks);
                    own_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                if (item.equals("Borrowed")) {
                    if (show==true){
                        Toast.makeText(getContext(), iitem.toString(),
                                Toast.LENGTH_SHORT).show();

                    }else {
                        show=true;
                    }
                    arrayOwnedbooks = dictBooks.get("Borrowed");
                    Log.d("byf", String.valueOf(arrayOwnedbooks.size()));
                    adapter = new bookListViewAdapter(getContext(), arrayOwnedbooks);
                    own_book_lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });*/

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"THIS IS CASE ============================"+requestCode);


            if(requestCode == 1) {// we are looking for the new information that the user edited the book.
                Log.d(TAG,"THIS IS CASE ONE AND --------------------"+requestCode);
                if (resultCode == Activity.RESULT_OK) {
                    String order = data.getStringExtra("do");
                    if (order.equals("edit")) {
                        currentBook = new Book("", "", "", false,"", null,"");
                        currentBook.setBookName(data.getStringExtra("BookName"));
                        currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                        currentBook.setDescription(data.getStringExtra("Description"));
                        currentBook.setClassification(data.getStringExtra("ClassificationArray"));
                        currentBook.setOwnerId(FirebaseAuth.getInstance().getUid());
                        Log.d("CURRENT BOOK: +++++++++",currentBook.getClassification());
                        Bitmap temp = (Bitmap) data.getParcelableExtra("BookCover");
                        String book_id = Long.toString(System.currentTimeMillis());
                        String ISBN = (data.getStringExtra("ISBN"));
                        //Log.d("THE ISBN IS ",ISBN);
                        if (ISBN != null){
                            book_id = ISBN;
                        }
                        currentBook.setID(book_id);
                        currentBook.setNew_status(BookStatus.available);
                        //Log.d(TAG,mAuth.getCurrentUser().getDisplayName());
                        currentBook.setOwnerName(mAuth.getCurrentUser().getDisplayName());
                        Util.uploadFile(temp,currentBook.getID(),currentBook,userID);
                        arrayOwnedbooks.add(currentBook);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            else if (requestCode == 2){// we are updating info of a book
                if (resultCode == Activity.RESULT_OK) {
                    String order = data.getStringExtra("do");
                    if (order.equals("edit")) {
                        if (!arrayOwnedbooks.isEmpty()) {
                            currentBook = arrayOwnedbooks.get(current_index);
                            currentBook.setBookName(data.getStringExtra("BookName"));
                            currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                            currentBook.setDescription(data.getStringExtra("Description"));
                            currentBook.setClassification(data.getStringExtra("ClassificationArray"));
                            Bitmap temp = (Bitmap) data.getParcelableExtra("BookCover");
                            Util.uploadFile(temp, currentBook.getID(), currentBook, userID);
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
}