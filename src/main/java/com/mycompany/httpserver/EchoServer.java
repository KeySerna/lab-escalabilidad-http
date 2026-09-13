/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver;

/**
 *
 * @author keysi
 */
import java.net.*;
import java.io.*;
 
public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        Socket clientSocket = serverSocket.accept();
 
        PrintWriter out = new PrintWriter(
            clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
 
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Message: " + inputLine);
            String outputLine = "Response: " + inputLine;
            out.println(outputLine);
            if (outputLine.equals("Response: Bye.")) break;
        }
 
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
