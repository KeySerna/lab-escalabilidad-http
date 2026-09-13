package com.mycompany.httpserver;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatApp {
    public static void main(String[] args) throws Exception {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("¿En qué puerto local quieres publicar tu chat?");
        int puertoLocal = Integer.parseInt(keyboard.readLine());

        ChatServiceImpl miServicio = new ChatServiceImpl();
        ChatService stub = (ChatService) UnicastRemoteObject.exportObject(miServicio, 0);
        Registry miRegistro = LocateRegistry.createRegistry(puertoLocal);
        miRegistro.rebind("chat", stub);
        System.out.println("Tu chat está publicado en el puerto " + puertoLocal);

        System.out.println("Escribe la IP remota:");
        String ipRemota = keyboard.readLine();
        System.out.println("Escribe el puerto remoto:");
        int puertoRemoto = Integer.parseInt(keyboard.readLine());

        Registry registroRemoto = LocateRegistry.getRegistry(ipRemota, puertoRemoto);
        ChatService chatRemoto = (ChatService) registroRemoto.lookup("chat");

        System.out.println("Ya puedes chatear. Escribe tus mensajes:");
        String linea;
        while ((linea = keyboard.readLine()) != null) {
            chatRemoto.recibirMensaje("Yo", linea);
        }
    }
}
