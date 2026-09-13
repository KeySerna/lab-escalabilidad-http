package com.mycompany.httpserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteEcho extends Remote {
    String echo(String text) throws RemoteException;
}