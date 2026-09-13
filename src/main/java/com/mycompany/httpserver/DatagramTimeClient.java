package com.mycompany.httpserver;

import java.net.*;
import java.io.*;

public class DatagramTimeClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        byte[] request = new byte[1];
        socket.send(new DatagramPacket(request, request.length, address, 4445));

        byte[] response = new byte[256];
        DatagramPacket packet = new DatagramPacket(response, response.length);
        socket.receive(packet);
        System.out.println("Date: "
            + new String(packet.getData(), 0, packet.getLength()));
        socket.close();
    }
}
