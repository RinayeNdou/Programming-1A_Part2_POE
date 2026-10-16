package com.st10470759.chatapp_part2;

import java.util.Scanner;
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

    // Runs the two test messages through sendingMessages.
    // Message 1 is sent and message 2 is disregarded (typed in for us, one line at a time).
    // 08575975889 is rejected by the recipient check, so message 2 uses a valid number here.
    private void runTestMessages() {
        String typedInput = "+27718693002\n"
                + "Hi Mike, can you join us for dinner tonight?\n"
                + "1\n"
                + "+27825975889\n"
                + "Hi Keegan, did you receive the payment?\n"
                + "2\n"
                + "0\n";
        Scanner input = new Scanner(typedInput);
        msg.setMessagesAllowed(2);
        msg.sendingMessages(input);
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

    // A message ID is created: it has 10 digits and the message says "Message ID generated: <ID>"
    @Test
    public void testMessageID_Created() {
        System.out.println("generateMessageID");
        String id = msg.generateMessageID();
        assertEquals("Message ID generated: " + id, msg.messageIDCreated(id));
        assertEquals(10, id.length());
        assertTrue(msg.checkMessageID(id));
    }

    // A ten-character ID is allowed
    @Test
    public void testCheckMessageID_Valid() {
        System.out.println("checkMessageID - valid");
        assertTrue(msg.checkMessageID("1234567890"));
    }

    // An ID longer than ten characters is not allowed
    @Test
    public void testCheckMessageID_Invalid() {
        System.out.println("checkMessageID - invalid");
        assertFalse(msg.checkMessageID("12345678901"));
    }

    // User selected 'Send Message'
    @Test
    public void testSentMessage_Send() {
        System.out.println("SentMessage - send");
        assertEquals("Message successfully sent.", msg.SentMessage(1));
    }

    // User selected 'Disregard Message'
    @Test
    public void testSentMessage_Disregard() {
        System.out.println("SentMessage - disregard");
        assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
    }

    // User selected 'Store Message'
    @Test
    public void testSentMessage_Store() {
        System.out.println("SentMessage - store");
        assertEquals("Message successfully stored.", msg.SentMessage(3));
    }

    // Message 1 is sent and message 2 is disregarded, so the total sent is 1
    @Test
    public void testReturnTotalMessages() {
        System.out.println("returnTotalMessages");
        runTestMessages();
        assertEquals(1, msg.returnTotalMessages());
    }

    // Only the sent message is listed, the disregarded one is not
    @Test
    public void testPrintMessages() {
        System.out.println("printMessages");
        runTestMessages();
        String result = msg.printMessages();
        assertTrue(result.contains("Hi Mike, can you join us for dinner tonight?"));
        assertTrue(result.contains("+27718693002"));
        assertFalse(result.contains("Keegan"));
    }

}