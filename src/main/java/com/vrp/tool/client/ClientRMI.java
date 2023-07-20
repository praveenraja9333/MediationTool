package com.vrp.tool.client;

import com.vrp.tool.models.Node;
import com.vrp.tool.models.RemoteObject;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

public class ClientRMI {


    public static void main(String[] args) {
        try {
            RemoteObject remoteObject =(RemoteObject) Naming.lookup("rmi://localhost:2001/admin");
           /* Map<String, Node> map=remoteObject.printHello();
            for(Map.Entry<String,Node> entry:map.entrySet()){
                System.out.println("Key  "+entry.getKey()+" Value "+entry.getValue());
            }*/
            System.out.println("String s "+remoteObject.getString());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
