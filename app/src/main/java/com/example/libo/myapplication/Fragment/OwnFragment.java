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
import android.widget.TextView;

import com.example.libo.myapplication.Activity.ItemViewActivity;
import com.example.libo.myapplication.Activity.MainActivity;
import com.example.libo.myapplication.Adapter.bookListViewAdapter;
import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class OwnFragment extends Fragment {

    private static final String TAG = "BorrowedBookDatabase";
    private TextView userNameTextView;
    ListView own_book_lv;
    ArrayAdapter<Book> adapter;
    private ArrayList<Book> arrayOwnedbooks;
    private Book currentBook;
    private int current_index = 0;

    private DatabaseReference databaseBook;
    private StorageReference storageRef;



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
        arrayOwnedbooks = new ArrayList<>();
        Button add_button = (Button) view.findViewById(R.id.AddButton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), ItemViewActivity.class);

                currentBook = new Book("", "", "", false,"", "","");
                intent.putExtra("BookName", currentBook.getBookName()); // Put the info of the book to next activity
                intent.putExtra("AuthorName", currentBook.getAuthorName());
                intent.putExtra("ID", currentBook.getID());
                intent.putExtra("status", currentBook.getStatus());
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
                ItemView.putExtra("status", currentBook.getStatus());
                ItemView.putExtra("edit",true);
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
        return view;


    }



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        SearchView searchView = getActivity().findViewById(R.id.searchView2);

        databaseBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayOwnedbooks.clear();

                Log.d(TAG,"Deading database successfully" + arrayOwnedbooks.toString());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Book book = ds.getValue(Book.class);
                    arrayOwnedbooks.add(book);

                }
                adapter = new bookListViewAdapter(getContext().getApplicationContext(), arrayOwnedbooks);
                //adapter = new ArrayAdapter<Book>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1,arrayOwnedbooks);
                own_book_lv.setAdapter(adapter);

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
        switch (requestCode) {

            case (1): {// we are looking for the new information that the user edited the book.
                Log.d(TAG,"THIS IS CASE ONE AND --------------------");
                if (resultCode == Activity.RESULT_OK) {
                    String order = data.getStringExtra("do");
                    if (order.equals("edit")) {
                        currentBook = new Book("", "", "", false,"", null,"");
                        currentBook.setBookName(data.getStringExtra("BookName"));
                        currentBook.setAuthorName(data.getStringExtra("AuthorName"));
                        currentBook.setDescription(data.getStringExtra("Description"));
                        currentBook.setClassification(data.getStringExtra("ClassificationArray"));
                        //currentBook.setBookCover((Bitmap) data.getParcelableExtra("BookCover"));
                        Log.d("CURRENT BOOK: +++++++++",currentBook.getClassification());
                        Bitmap temp = (Bitmap) data.getParcelableExtra("BookCover");
                        String book_id = databaseBook.push().getKey();
                        currentBook.setID(book_id);
                        uploadFile(temp,currentBook.getID(),currentBook);
                    }
                }
            }
            case (2): {// we are updating info of a book
                Log.d(TAG,"THIS IS CASE TWo AND +++++++++++++++++++++++");
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
                            String bookID = currentBook.getID();
                            uploadFile(temp, currentBook.getID(), currentBook);
                        }
                    }
                }
            }
        }
    }

    private void uploadFile(Bitmap bookCover, final String id, final Book data_book){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference("book_photo").child("image/"+id+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bookCover.compress(Bitmap.CompressFormat.JPEG,20,baos);
        byte[] data = baos.toByteArray();
        final UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Upload image fail",e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)  {
                        if (!task.isSuccessful()){
                            Log.i("problem", task.getException().toString());
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            Log.i("seeThisUri", downloadUri.toString());
                            data_book.setBookcoverUri(downloadUri.toString());
                            Log.d(TAG,"==============================================");
                            databaseBook.child(id).setValue(data_book);
                        }
                    }
                });
            }
        });
    }


}