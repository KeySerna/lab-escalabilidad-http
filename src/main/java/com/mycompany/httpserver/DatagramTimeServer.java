package com.mycompany.httpserver;

import java.net.*;
import java.io.*;
import java.util.Date;

public class DatagramTimeServer {
    private DatagramSocket socket;

    public DatagramTimeServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void startServer() throws IOException {
        while (true) {
            byte[] buffer = new byte[256];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            socket.receive(request);

            byte[] response = new Date().toString().getBytes();
            DatagramPacket packet = new DatagramPacket(
                response, response.length, request.getAddress(), request.getPort());
            socket.send(packet);
            System.out.println("Solicitud atendida.");
        }
    }

    public static void main(String[] args) throws IOException {
        DatagramTimeServer server = new DatagramTimeServer();
        System.out.println("Servidor de hora listo...");
        server.startServer();
    }
}