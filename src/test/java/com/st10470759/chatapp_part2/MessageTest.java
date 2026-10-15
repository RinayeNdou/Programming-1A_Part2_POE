package com.st10470759.chatapp_part2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rnndo
 */
public class MessageTest {

    private Message msg;

    // Runs before every test so each test starts with a fresh Message object
    @BeforeEach
    public void setUp() {
        msg = new Message();
    }

    // Message is not more than 250 characters (success)
    @Test
    public void testMessageLength_Success() {
        System.out.println("checkMessageLength - success");
        String text = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("Message ready to send.", msg.checkMessageLength(text));
    }

    // Message is more than 250 characters (failure)
    // "a".repeat(255) makes a message of 255 letters, which is 5 too many
    @Test
    public void testMessageLength_Failure() {
        System.out.println("checkMessageLength - failure");
        String text = "a".repeat(255);
        assertEquals("Message exceeds 250 characters by 5; please reduce the size.", msg.checkMessageLength(text));
    }

    // Recipient number is correctly formatted (success)
    @Test
    public void testRecipientCell_Success() {
        System.out.println("checkRecipientCell - success");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell("+27718693002"));
    }

    // Recipient number is incorrectly formatted (failure)
    @Test
    public void testRecipientCell_Failure() {
        System.out.println("checkRecipientCell - failure");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell("08575975889"));
    }

}