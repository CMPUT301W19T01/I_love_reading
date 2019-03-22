package com.example.libo.myapplication.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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

public class BorrowFragment extends Fragment {

    private Button button;

    private TextView userNameTextView;

    ListView borrow_book_lv;

    private static final String TAG = "BorrowedBookDatabase";

    ArrayAdapter<Book> adapter;
    private ArrayList<Book> borrowedBooks;

    private String Userid;
    private DatabaseReference borrowedRef;
    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.borrow_page,container,false);

        borrow_book_lv = (ListView)view.findViewById(R.id.borrow_book);

        Userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        borrowedRef = FirebaseDatabase.getInstance().getReference("borrowedBooks").child(Userid);

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
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                borrowedBooks.clear();
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

                        borrowedBooks.add(book);

                    }
                }
                adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,borrowedBooks);
                borrow_book_lv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
