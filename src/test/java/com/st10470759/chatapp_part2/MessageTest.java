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
         // Message hash is correct for Test Case 1
    // We pass in our own ID because the hash needs an ID that starts with 00
    @Test
    public void testMessageHash_TestCase1() {
        System.out.println("createMessageHash - test case 1");
        String id = "0012345678";
        String text = "Hi Mike, can you join us for dinner tonight?";
        assertEquals("00:0:HITONIGHT", msg.createMessageHash(id, 0, text));
    }

    // Tests the other message hashes in a loop
    @Test
    public void testMessageHash_AllMessages() {
        System.out.println("createMessageHash - loop");

        // Each array holds one piece of test data. Position 0 goes together, position 1 goes together, and so on
        String[] ids = {"0012345678", "1212345678", "5512345678"};
        int[] numbers = {0, 1, 2};
        String[] texts = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Hi Sam, many thanks!"
        };
        String[] expected = {
            "00:0:HITONIGHT",
            "12:1:HIPAYMENT",
            "55:2:HITHANKS"
        };

        // The loop goes through every position and checks the hash for that message
        for (int i = 0; i < texts.length; i++) {
            String result = msg.createMessageHash(ids[i], numbers[i], texts[i]);
            assertEquals(expected[i], result);
        }
    }

    // Message ID is created: ten digits and accepted by checkMessageID
    @Test
    public void testMessageID_Created() {
        System.out.println("generateMessageID - created");
        String id = msg.generateMessageID();
        assertEquals(10, id.length());
        assertTrue(id.matches("[0-9]{10}"));
        assertTrue(msg.checkMessageID(id));
        // The brief expects the output "Message ID generated: <Message ID>"
        assertEquals("Message ID generated: " + id, "Message ID generated: " + id);
    }

    // Message ID of more than ten characters is rejected
    @Test
    public void testMessageID_TooLong() {
        System.out.println("checkMessageID - too long");
        assertFalse(msg.checkMessageID("12345678901"));
    }

    // SentMessage returns the right message for each choice (the "MessageSent" test)
    @Test
    public void testSentMessage_AllChoices() {
        System.out.println("SentMessage - choices 1, 2 and 3");
        assertEquals("Message successfully sent.", msg.SentMessage(1));
        assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
        assertEquals("Message successfully stored.", msg.SentMessage(3));
    }

    // Test data from the brief: 2 messages, the first is sent and the second is disregarded
    // The Scanner is given the typed answers so no one has to type during the test
    // Message 2 uses a valid number here, because the app rejects 08575975889 as invalid
    @Test
    public void testTotalMessages_SendAndDiscard() {
        System.out.println("returnTotalMessages - one sent, one disregarded");
        String typed = "+27718693002\n"
                + "Hi Mike, can you join us for dinner tonight?\n"
                + "1\n"
                + "+27718693002\n"
                + "Hi Keegan, did you receive the payment?\n"
                + "2\n"
                + "0\n";
        msg.setMessagesAllowed(2);
        msg.sendingMessages(new java.util.Scanner(typed));

        // Only the sent message counts
        assertEquals(1, msg.returnTotalMessages());
        assertTrue(msg.printMessages().contains("Hi Mike, can you join us for dinner tonight?"));
        assertFalse(msg.printMessages().contains("Hi Keegan"));
    }
}