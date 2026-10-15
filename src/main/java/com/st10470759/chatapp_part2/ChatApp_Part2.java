/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.st10470759.chatapp_part2;

/**
 *
 * @author rnndo
 */
public class ChatApp_Part2 {

    public static void main(String[] args) {
    Login login = new Login();
    System.out.println(login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27718693002", "Kyle", "Smith"));
    boolean ok = login.loginUser("kyl_1", "Ch&&sec@ke99!");
    System.out.println(ok);
    System.out.println(login.returnLoginStatus(ok));
}
}
