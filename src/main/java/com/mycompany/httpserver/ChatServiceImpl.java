/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author keysi
 */
package com.mycompany.httpserver;

import java.rmi.RemoteException;

public class ChatServiceImpl implements ChatService {
    public void recibirMensaje(String remitente, String mensaje) throws RemoteException {
        System.out.println("\n" + remitente + " dice: " + mensaje);
    }
}