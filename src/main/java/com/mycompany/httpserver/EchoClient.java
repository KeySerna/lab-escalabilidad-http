/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver;

/**
 *
 * @author keysi
 */
import java.io.*;
import java.net.*;
 
public class EchoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 35000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(
            new InputStreamReader(System.in));
 
        String userInput;
        while ((userInput = keyboard.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine());
        }
 
        out.close();
        in.close();
        keyboard.close();
        socket.close();
    }
}