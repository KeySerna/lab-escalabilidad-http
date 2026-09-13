/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver;

import java.io.*;
import java.net.*;

public class MiniBrowser {

    public static void main(String[] args) throws IOException, URISyntaxException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Escribe una URL:");
        String urlTexto = keyboard.readLine();

        URI miURI = new URI(urlTexto);
        URL miURL = miURI.toURL();

        try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(miURL.openStream(), java.nio.charset.StandardCharsets.UTF_8));
     PrintWriter writer = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream("result.html"), java.nio.charset.StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                writer.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
