package com.example.libo.myapplication.Fragment;

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
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllFragment extends Fragment {
    private static final String TAG = "AllBookDatabase";
    private Button button;

    private TextView userNameTextView;

    ListView all_book_lv;

    ArrayAdapter<Book> adapter;
    private DatabaseReference AlldatabaseBook;
    private ArrayList<Book> arrayAllbooks;
    private String userid;
    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.all_page,container,false);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        all_book_lv = (ListView)view.findViewById(R.id.all_book);
        arrayAllbooks = new ArrayList<>();
        AlldatabaseBook = FirebaseDatabase.getInstance().getReference("books").child(userid);
        adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayAllbooks);
        AlldatabaseBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d(TAG,"All database successfully");
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Book book = ds.getValue(Book.class);
                    /*
                    Log.d(TAG,"ALL BOOK TAG" +ds.toString());
                    Log.d(TAG,"ALL Book name" + book.getBookName());
                    */
                    ArrayList<String> Classification = new ArrayList<String>();

                    book.setClassification(Classification);

                    Bitmap bitmap = Bitmap.createBitmap(5,5,Bitmap.Config.ARGB_8888);
                    Comment comment_4 = new Comment(2.5,"海南蹦迪王","2018/9/9", "I hate 301！！！！！！！！！！！！！！！！！！");

                    book.addComments(comment_4);

                    arrayAllbooks.add(book);

                }
                adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayAllbooks);
                all_book_lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        all_book_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent ItemView = new Intent(getActivity().getApplication(),ItemViewActivity.class); // set the intent to start next activity
                Book currentBook = arrayAllbooks.get(i);
                ItemView.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                ItemView.putExtra("AuthorName", currentBook.getAuthorName());
                ItemView.putExtra("ID", currentBook.getID());
                ItemView.putExtra("status", currentBook.getStatus());
                Log.d(TAG,"the current status is " + currentBook.getStatus().toString());
                ItemView.putExtra("edit",false);
                ItemView.putExtra("Description", currentBook.getDescription());
                ItemView.putExtra("ClassificationArray", currentBook.getClassification());
                ItemView.putExtra("BookCover", currentBook.getBookCover());
                ItemView.putExtra("CommentArray",currentBook.getComments());

                startActivityForResult(ItemView, 0); // request code 0 means we are looking for if the user decide to borrow the book
            }
        });


        adapter = new ArrayAdapter<Book>(getContext(),android.R.layout.simple_list_item_1,arrayAllbooks);

        all_book_lv.setAdapter(adapter);

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

}
