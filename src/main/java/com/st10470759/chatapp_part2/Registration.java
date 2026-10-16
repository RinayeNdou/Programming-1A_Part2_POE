/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.st10470759.chatapp_part2;

import java.util.Scanner;
/**
 *
 * @author rnndo
 */
public class Registration {

 public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        Login login = new Login();
        Message msg = new Message();

        // ----- Registration (same as Part 1) -----
        System.out.println("Please enter a username:");
        String username = scan.nextLine();

        System.out.println("Please enter a password:");
        String password = scan.nextLine();

        System.out.println("Please enter a South African cell phone number (e.g. +27831234567):");
        String cellPhoneNumber = scan.nextLine();

        System.out.println("Please enter your first name:");
        String firstName = scan.nextLine();

        System.out.println("Please enter your last name:");
        String lastName = scan.nextLine();

        String registrationMessage = login.registerUser(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(registrationMessage);

        boolean registrationSuccessful = login.checkUserName(username) && login.checkPasswordComplexity(password) && login.checkCellPhoneNumber(cellPhoneNumber);

        if (registrationSuccessful) {

            // ----- Login -----
            System.out.println("\nNow please log in.");

            System.out.println("Enter your username:");
            String loginUsername = scan.nextLine();

            System.out.println("Enter your password:");
            String loginPassword = scan.nextLine();

            boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(loginSuccess));

            // Users can only send messages if they logged in successfully
            if (loginSuccess) {

                System.out.println("\nWelcome to QuickChat.");

                System.out.print("How many messages do you want to enter? ");
                int numberOfMessages = scan.nextInt();
                scan.nextLine();
                msg.setMessagesAllowed(numberOfMessages);

                // The menu keeps showing until the user chooses 3 (Quit)
                int option;
                do {
                    System.out.println("\n===== Menu =====");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.print("Enter your choice: ");
                    option = scan.nextInt();
                    scan.nextLine();

                    switch (option) {
                        case 1 -> msg.sendingMessages(scan);
                        case 2 -> System.out.println("Coming Soon.");
                        case 3 -> System.out.println("Goodbye.");
                        default -> System.out.println("Invalid option, please try again.");
                    }
                } while (option != 3);
            }
        }

        scan.close();
    }
}