package com.example.libo.myapplication.Fragment;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrowFragment extends Fragment {

    private Button button;

    private TextView userNameTextView;

    ListView borrow_book_lv;

    private static final String TAG = "BorrowedBookDatabase";

    ArrayAdapter<Book> adapter;
    private ArrayList<Book> arrayBorrowbooks;
    private ArrayList<String> borrowedbook;
    private String Userid;
    private DatabaseReference borrowedRef;
    private DatabaseReference booksRef;
    private int current_index = 0;
    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.borrow_page,container,false);


        borrow_book_lv = (ListView)view.findViewById(R.id.borrow_book);

        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        booksRef = FirebaseDatabase.getInstance().getReference("books");

        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(Userid);

        borrowedbook = new ArrayList<String>();

        borrowedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(!borrowedbook.contains(ds.getKey())){
                        Log.d(TAG,"ds.getKey is " + ds.getKey());
                        borrowedbook.add(ds.getKey());
                        Log.d(TAG,"THE current array is " + borrowedbook.toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        borrow_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(), ItemViewActivity.class); // set the intent to start next activity
                Book currentBook = arrayBorrowbooks.get(i);
                //imageUri = Book.getBookCover();
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
                ItemView.putExtra("CommentArray",currentBook.getComments());
                current_index = i;
                startActivityForResult(ItemView, 2); // request code 2 means we are updating info of a book
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

        Log.d(TAG,"the arraylist is " + borrowedbook.isEmpty());

       //Log.d(TAG,"the arraylist is " + borrowedbok.isEmpty());
        /*
        Log.d(TAG,"The current key is ===========" + borrowedRef.getRoot());
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //arrayBorrowbooks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot newds : ds.getChildren()) {

                        Log.d(TAG,"ALL BOOK TAG newwd is :     -------" +newds.toString());
                        Book book = newds.getValue(Book.class);
                        Log.d(TAG,"ALL Book name" + book.getBookName());





                        ArrayList<String> Classification = new ArrayList<String>();

                        book.setClassification(Classification);

                        Bitmap bitmap = Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_8888);
                        Comment comment_4 = new Comment(2.5, "海南蹦迪王", "2018/9/9", "I hate 301！！！！！！！！！！！！！！！！！！");

                        book.addComments(comment_4);

                        arrayBorrowbooks.add(book);

                    }
                }
                adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayBorrowbooks);
                borrow_book_lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    */

    }


}
