package com.vrp.tool.models;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteObject extends Remote {
    Map<String, Node> printHello() throws RemoteException;
    String getString() throws RemoteException;
}
