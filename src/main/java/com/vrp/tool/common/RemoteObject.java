package com.vrp.tool.common;

import com.vrp.tool.models.Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteObject extends Remote {
    Map<String, Node> printHello() throws RemoteException;
    RMIResponse<String> getString(SessionKey sessionKey) throws RemoteException;

    RMIResponse<SessionKey> register(ClientHeaders clientHeaders) throws RemoteException;
    boolean unregister(SessionKey sessionKey) throws RemoteException;
}
