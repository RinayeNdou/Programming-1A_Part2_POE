/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.st10470759.chatapp_part2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author rnndo
 */
public class Message {

    // Lists that hold the details of every message that was SENT
    ArrayList<String> sentMessages = new ArrayList<>();
    ArrayList<String> messageIdsList = new ArrayList<>();
    ArrayList<String> msgHash = new ArrayList<>();
    ArrayList<String> recipientNumber = new ArrayList<>();

    // Remembers every ID we have made so no two messages get the same ID
    HashSet<String> messageIds = new HashSet<>();

    // Holds the messages the user chose to STORE (these are written to a JSON file)
    JsonArray storedMessages = new JsonArray();

    Random random = new Random();

    // We reuse the Login class from Part 1 to check the recipient number
    Login login = new Login();

    int numMessageSent = 0;    // how many messages have been sent so far
    int messagesAllowed = 0;   // how many messages the user said they want to enter
    int messagesEntered = 0;   // how many messages the user has dealt with so far

    // Saves how many messages the user wants to enter
    public void setMessagesAllowed(int number) {
        messagesAllowed = number;
    }

    // Asks the user for each message and lets them send, disregard or store it
    public void sendingMessages(Scanner input) {

        if (messagesEntered >= messagesAllowed) {
            System.out.println("You have already entered all " + messagesAllowed + " messages.");
            return;
        }

        while (messagesEntered < messagesAllowed) {

            System.out.println("\n==============================");
            System.out.println("MESSAGE " + (messagesEntered + 1) + " of " + messagesAllowed);
            System.out.println("==============================");

            // Recipient
            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();

            String recipientResult = checkRecipientCell(recipient);
            System.out.println(recipientResult);
            if (!login.checkCellPhoneNumber(recipient)) {
                continue;   // go back and ask for this message again
            }

            // Message text
            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            if (messageText.trim().isEmpty()) {
                System.out.println("The message cannot be empty.");
                continue;
            }
            if (messageText.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.println(checkMessageLength(messageText));
                continue;
            }
            System.out.println(checkMessageLength(messageText));

            // ID and hash
            String messageID = generateMessageID();
            System.out.println("Message ID generated: " + messageID);
            String messageHash = createMessageHash(messageID, numMessageSent, messageText);

            // Ask what to do with the message (keeps asking until the choice is 1, 2 or 3)
            int choice = 0;
            while (choice < 1 || choice > 3) {
                System.out.println("\nWhat do you want to do with this message?");
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Enter your choice: ");
                choice = input.nextInt();
                input.nextLine();

                if (choice < 1 || choice > 3) {
                    System.out.println(SentMessage(choice));
                }
            }

            // For choice 3 the message is only shown after the file was saved (see below)
            if (choice != 3) {
                System.out.println(SentMessage(choice));
            }

            if (choice == 1) {
                // Send: save the message in the lists
                sentMessages.add(messageText);
                messageIdsList.add(messageID);
                msgHash.add(messageHash);
                recipientNumber.add(recipient);
                numMessageSent++;

                System.out.println("\n" + messageDetails(sentMessages.size() - 1));

            } else if (choice == 2) {
                // Disregard: the message is not saved anywhere
                int deleteChoice = input.nextInt();
                input.nextLine();
                if (deleteChoice == 0) {
                    System.out.println("Message deleted.");
                } else {
                    System.out.println("Message disregarded.");
                }

            } else {
                // Store: write the message to the JSON file
                boolean saved = storeMessage(messageID, messageHash, recipient, messageText);
                if (saved) {
                    System.out.println(SentMessage(choice));
                } else {
                    System.out.println("Sorry, the message could not be saved to the file.");
                }
            }

            messagesEntered++;
        }

        System.out.println("\n===== All messages sent =====");
        System.out.println(printMessages());
        System.out.println("Total number of messages sent: " + returnTotalMessages());
    }

    // Makes a random ten-digit ID that has not been used before
    public String generateMessageID() {
        String messageID;
        do {
            long number = 1000000000L + random.nextLong(9000000000L);
            messageID = String.valueOf(number);
        } while (messageIds.contains(messageID) || !checkMessageID(messageID));

        messageIds.add(messageID);
        return messageID;
    }

    // Checks that the message ID is not more than ten characters long
    public boolean checkMessageID(String messageID) {
        if (messageID == null) {
            return false;
        }
        return messageID.length() <= 10;
    }

    // Checks the recipient number by reusing checkCellPhoneNumber from Part 1
    // (+27 followed by 9 or 10 digits), and returns the Part 2 messages
    public String checkRecipientCell(String cellphone) {
        if (login.checkCellPhoneNumber(cellphone)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Checks that the message is not more than 250 characters
    public String checkMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int extra = messageText.length() - 250;
            return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
        }
    }

    // Makes the hash: first two digits of the ID, the message number, then the first and last word in capitals
    public String createMessageHash(String messageID, int messageNumber, String messageText) {

        String firstTwoNumbers = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        // replaceAll removes anything that is not a letter or a number (like the ? in "tonight?")
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        String hash = firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord;

        return hash.toUpperCase();
    }

    // Returns the right message for the choice: 1 = send, 2 = disregard, 3 = store
    public String SentMessage(int choice) {
        if (choice == 1) {
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            return "Message successfully stored.";
        } else {
            return "Invalid option, please try again.";
        }
    }

    // Stores a message in a JSON file (storedMessages.json) so it can be sent later
    // Returns true if the file was saved and false if something went wrong
    // Reference: Google (n.d.) Gson User Guide. Available at: https://google.github.io/gson/UserGuide.html (Accessed: 15 October 2026).
    public boolean storeMessage(String messageID, String messageHash, String recipient, String messageText) {

        // Put the details of this message into a JSON object
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("messageID", messageID);
        messageObject.addProperty("messageHash", messageHash);
        messageObject.addProperty("recipient", recipient);
        messageObject.addProperty("message", messageText);

        // Add it to the list of stored messages
        storedMessages.add(messageObject);

        // Write the whole list of stored messages to the file
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try {
            FileWriter writer = new FileWriter("storedMessages.json");
            gson.toJson(storedMessages, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Returns the details of one sent message: ID, hash, recipient, message
    private String messageDetails(int index) {
        return "Message ID: " + messageIdsList.get(index)
                + "\nMessage Hash: " + msgHash.get(index)
                + "\nRecipient: " + recipientNumber.get(index)
                + "\nMessage: " + sentMessages.get(index) + "\n";
    }

    // Returns the details of all the messages that were sent
    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            allMessages.append(messageDetails(i)).append("\n");
        }
        return allMessages.toString();
    }

    // Returns how many messages have been sent
    public int returnTotalMessages() {
        return numMessageSent;
    }

}
