package programming3.chatsys.data;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    Database db;

    @BeforeEach
    void setUp() {
        File file = new File(".\\database_test.txt");
        ChatMessage cm1 = new ChatMessage(1, "Jack_1", new Timestamp(100000), "Haloo");
        ChatMessage cm2 = new ChatMessage(2, "Ana", new Timestamp(200000), "Hello");
        cm1.save(file);
        cm2.save(file);
        db = new Database(".\\database_test.txt");
    }

    @AfterEach
    void tearDown() {
        db = null;
        File file = new File(".\\database_test.txt");
        file.delete();
    }

    @Test
    void readMessages() {
        ChatMessage[] msgArray = new ChatMessage[] {
                new ChatMessage(1, "Jack_1", new Timestamp(100000), "Haloo"),
                new ChatMessage(2, "Ana", new Timestamp(200000), "Hello"),
        };
        List<ChatMessage> msgList = Arrays.asList(msgArray);
        Assertions.assertEquals(msgList, db.readMessages());
    }

    @Test
    void readUsers() {
        File file = new File(".\\database_test.txt");
        file.delete();

        User user1 = new User("Jack", "JackMa", "666666");
        User user2 = new User("Jason", "JasonWu", "123456");
        user1.save(file);
        user2.save(file);
        Database db = new Database(".\\database_test.txt");

        Map<String, User> userMap = new HashMap<String, User>();
        userMap.put("Jack", user1);
        userMap.put("Jason", user2);
        Assertions.assertEquals(userMap, db.readUsers());
    }

    @Test
    void addMessage() {
        ChatMessage cm = new ChatMessage(5, "Jack", new Timestamp(1000000), "Hello World");
        try {
            db.addMessage(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ChatMessage> msgList1 = db.readMessages();

        ChatMessage[] msgArray = new ChatMessage[] {
                new ChatMessage(1, "Jack_1", new Timestamp(100000), "Haloo"),
                new ChatMessage(2, "Ana", new Timestamp(200000), "Hello"),
                new ChatMessage(5, "Jack", new Timestamp(1000000), "Hello World")
        };
        List<ChatMessage> msgList2 = Arrays.asList(msgArray);
        Assertions.assertEquals(msgList2, msgList1);
    }

    @Test
    void testIDShouldGreaterThanAllOtherIDs() {
        ChatMessage cm = new ChatMessage(2, "Jack", new Timestamp(1000000), "Hello World");
        Assertions.assertThrows(Exception.class, () -> {
            db.addMessage(cm);
        });
    }

}