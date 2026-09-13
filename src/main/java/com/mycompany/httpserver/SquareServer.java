package com.mycompany.httpserver;

/**
 *
 * @author keysi
 */
import java.net.*;
import java.io.*;

public class SquareServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        Socket clientSocket = serverSocket.accept();

        PrintWriter out = new PrintWriter(
            clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);

            double numero = Double.parseDouble(inputLine);
            double cuadrado = numero * numero;
            String outputLine = "El cuadrado de " + numero + " es " + cuadrado;

            out.println(outputLine);
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
