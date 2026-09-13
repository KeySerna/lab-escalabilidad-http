package com.mycompany.httpserver;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RemoteEchoServer implements RemoteEcho {

    public String echo(String text) throws RemoteException {
        return "from the server: " + text;
    }

    public static void main(String[] args) {
        try {
            RemoteEchoServer serverObj = new RemoteEchoServer();
            RemoteEcho stub = (RemoteEcho) UnicastRemoteObject.exportObject(serverObj, 0);

            Registry registry = LocateRegistry.createRegistry(23000);
            registry.rebind("EchoService", stub);

            System.out.println("Servidor RMI listo...");
        } catch (Exception e) {
            System.err.println("Error en el servidor:");
            e.printStackTrace();
        }
    }
}