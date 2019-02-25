package com.example.libo.myapplication;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.Model.Users;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleUnitTest {
    @Test
    public void request_isCorrect() {
        Request re = new Request("user1","user2","user1@qq.com");
        String sender, receiver, senderEmail;
        sender = re.getSender();
        receiver = re.getReceiver();
        senderEmail = re.getSenderEmail();
        assertEquals("user1",sender);
        assertEquals("user2",receiver);
        assertEquals("user1@qq.com",senderEmail);
    }

    @Test
    public void users_isCorrect() {
        Users user1 = new Users("ybai5@gmail.com","Phoebe","123456");
        String email,username,password;
        email = user1.getEmail();
        username = user1.getUsername();
        password = user1.getPassword();

        assertEquals("ybai5@gmail.com",email);
        assertEquals("Phoebe",username);
        assertEquals("123456",password);
    }
    @Test
    public void book_correct(){
        Book test = new Book("Harry potter", "I don't know", "IDDDDD", false,"No description");
        assertEquals("Harry potter", test.getBookName());
        assertEquals("I don't know", test.getAuthorName());
        assertEquals("IDDDDD", test.getID());
        assertEquals(false, test.getStatus());
    }
}
