/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author keysi
 */
package com.mycompany.httpserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    void recibirMensaje(String remitente, String mensaje) throws RemoteException;
}
