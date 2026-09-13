package com.mycompany.httpserver;

/**
 *
 * @author keysi
 */
import java.net.*;
import java.io.*;

public class TrigServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(35000);
        Socket clientSocket = serverSocket.accept();

        PrintWriter out = new PrintWriter(
            clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));

        String funcionActual = "cos";

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            String outputLine;

            if (inputLine.startsWith("fun:")) {
                funcionActual = inputLine.substring(4);
                outputLine = "Función cambiada a: " + funcionActual;
            } else {
                double numero = Double.parseDouble(inputLine);
                double resultado;
                switch (funcionActual) {
                    case "sin":
                        resultado = Math.sin(numero);
                        break;
                    case "tan":
                        resultado = Math.tan(numero);
                        break;
                    default:
                        resultado = Math.cos(numero);
                        break;
                }
                outputLine = funcionActual + "(" + numero + ") = " + resultado;
            }

            out.println(outputLine);
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}