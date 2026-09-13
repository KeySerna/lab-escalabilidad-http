package com.mycompany.httpserver;

import java.net.*;
import java.io.*;

public class DatagramTimeClientResiliente {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("127.0.0.1");
        socket.setSoTimeout(2000); // espera máximo 2 segundos por respuesta

        String ultimaHoraConocida = "Sin datos aún";

        while (true) {
            try {
                byte[] request = new byte[1];
                socket.send(new DatagramPacket(request, request.length, address, 4445));

                byte[] response = new byte[256];
                DatagramPacket packet = new DatagramPacket(response, response.length);
                socket.receive(packet);

                ultimaHoraConocida = new String(packet.getData(), 0, packet.getLength());
            } catch (SocketTimeoutException e) {
                System.out.println("(El servidor no respondió, mostrando el último valor conocido)");
            }

            System.out.println("Hora actual del servidor: " + ultimaHoraConocida);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}