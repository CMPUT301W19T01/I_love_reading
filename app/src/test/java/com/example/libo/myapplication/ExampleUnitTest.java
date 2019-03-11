package com.example.libo.myapplication;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.Model.Users;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
    @Test
    public void request_isCorrect() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Request re = new Request();
        re.setSender("user1");
        re.setReceiver("user2");
        re.setSenderEmail("123@qq.com");
        re.setBorrowed(true);
        re.setDate(date);
        assertEquals("user1", re.getSender());
        assertEquals("user2",re.getReceiver());
        assertEquals("123@qq.com",re.getSenderEmail());
        assertEquals(true,re.isBorrowed());
        assertEquals(date, re.getDate());


        Request re1 = new Request("user1", "user2", "user1@qq.com", false, date);
        String sender, receiver, senderEmail;
        sender = re1.getSender();
        receiver = re1.getReceiver();
        senderEmail = re1.getSenderEmail();
        assertEquals("user1",sender);
        assertEquals("user2",receiver);
        assertEquals("user1@qq.com",senderEmail);
        assertEquals(date, re1.getDate());

    }

    @Test
    public void comment_isCorrect(){
        Comment comment = new Comment();
        comment.setRating(9.0);
        comment.setContent("comment");
        comment.setTime("2019-3-4 10:10");
        comment.setUsername("user1");

        assertEquals(9.0,comment.getRating(),0.01);
        assertEquals("comment", comment.getContent());
        assertEquals("2019-3-4 10:10", comment.getTime());
        assertEquals("user1",comment.getUsername());
        
    }

    @Test
    public void users_isCorrect() {

        Users user1 = new Users();
        user1.setEmail("ybai5@gmail.com");
        user1.setUsername("Phoebe");
        user1.setUid("123456");
        String email,username,password;
        email = user1.getEmail();
        username = user1.getUsername();
        password = user1.getUid();

        assertEquals("ybai5@gmail.com",email);
        assertEquals("Phoebe",username);
        assertEquals("123456",password);

    }

    @Test
    public void book_correct(){
        ArrayList<String> classficationList = new ArrayList<String>();
        classficationList.add("sci-fic");
        ArrayList<Comment> commentsList = new ArrayList<Comment>();
        Comment comment = new Comment(9,"user1","2019-10-9 10:00","This book is interesting");
        commentsList.add(comment);

        Book test1 = new Book();
        test1.setBookName("Harry Potter");
        test1.setAuthorName("I don't know");
        test1.setID("IDD");
        test1.setStatus(true);
        test1.setDescription("Boring");
        test1.setClassification(classficationList);
        assertEquals("Harry potter", test1.getBookName());
        assertEquals("I don't know", test1.getAuthorName());
        assertEquals("IDD", test1.getID());
        assertEquals(false, test1.getStatus());
        assertEquals(classficationList,test1.getClassification());
        assertEquals("Boring",test1.getDescription());



        Book test = new Book("Harry potter","I don't know","ID",false,"description",classficationList);
        assertEquals("Harry potter", test.getBookName());
        assertEquals("I don't know", test.getAuthorName());
        assertEquals("IDDDDD", test.getID());
        assertEquals(false, test.getStatus());
        assertEquals(classficationList,test.getClassification());

    }
}
