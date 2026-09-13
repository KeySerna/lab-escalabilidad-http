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

public class URLReader {

    public static void main(String[] args) throws Exception {
        URI googleURI = new URI("http://www.google.com/");
        URL google = googleURI.toURL();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(google.openStream()))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
