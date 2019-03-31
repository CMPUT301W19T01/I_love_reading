package com.example.libo.myapplication;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Request;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static DatabaseReference FirebaseRequests = FirebaseDatabase.getInstance().getReference("requests");
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");
    public static void SendRequset(String bookOwner, Book book, Boolean isBorrowed) {

        Request request = new Request();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String sender =currentUser.getDisplayName();
        String sender_email = currentUser.getEmail();
        Date current_date = Calendar.getInstance().getTime();
        String senderId = currentUser.getUid();


        String requestid = FirebaseRequests.push().getKey();
        request.setDate(current_date);
        request.setReceiver(bookOwner);
        request.setSender(sender);
        request.setSenderEmail(sender_email);
        request.setSenderId(senderId);
        request.setBookId(book.getID());
        request.setBookName(book.getBookName());
        request.setBorrowed(isBorrowed);
        request.setRequestId(requestid);
        FirebaseRequests.child(bookOwner).child(requestid).setValue(request);
        book.setNew_status(BookStatus.requested);
        databaseReference.child(book.getOwnerId()).child(book.getID()).setValue(book);
    }


    public static void uploadFile(Bitmap bookCover, final String id, final Book data_book, final String userid){

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
                            databaseReference.child(userid).child(id).setValue(data_book);
                        }
                    }
                });
            }
        });
    }
}
