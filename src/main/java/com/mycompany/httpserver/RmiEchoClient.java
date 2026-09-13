package com.mycompany.httpserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiEchoClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 23000);
            RemoteEcho service = (RemoteEcho) registry.lookup("EchoService");
            System.out.println(service.echo("Hola, ¿como estas?"));
        } catch (Exception e) {
            System.err.println("Hay un problema:");
            e.printStackTrace();
        }
    }
}