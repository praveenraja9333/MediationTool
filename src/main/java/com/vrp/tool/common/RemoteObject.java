package com.vrp.tool.common;

import com.vrp.tool.models.Node;
import lombok.Synchronized;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteObject extends Remote {
    Map<String, Node> printHello() throws RemoteException;
    RMIResponse<String> getString(SessionKey sessionKey) throws RemoteException;

    RMIResponse<Object> register(ClientHeaders clientHeaders) throws RemoteException;
    boolean unregister(SessionKey sessionKey) throws RemoteException;

    RMIResponse<Object> getScheduledJobs(RMIRequest rmiRequest) throws RemoteException;
    RMIResponse<Object> getInstalledNodes(RMIRequest rmiRequest) throws RemoteException;
    RMIResponse<Object> getInstalledJobs(RMIRequest rmiRequest) throws RemoteException;
    RMIResponse<Object> installNodes(RMIRequest rmiRequest) throws RemoteException;
    RMIResponse<Object> removeNodes(RMIRequest rmiRequest) throws RemoteException;
    RMIResponse<Object> removeJobs(RMIRequest rmiRequest) throws RemoteException;

    RMIResponse<Object> reInstallJobs(RMIRequest rmiRequest) throws RemoteException;


}
