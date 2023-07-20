package com.vrp.tool.admin;

import com.vrp.tool.models.File;
import com.vrp.tool.models.Node;
import com.vrp.tool.models.RemoteObject;
import com.vrp.tool.service.JobServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Map;

@Component
public class AdminRMI extends UnicastRemoteObject implements RemoteObject {
    @Autowired
    private JobServiceFactory jobServiceFactory;
    public AdminRMI() throws RemoteException {
    }

    @Override
    public Map<String, Node>printHello() {
        return jobServiceFactory.getInstalledNodes();
    }

    @Override
    public String getString() throws RemoteException {
        return "remoteAdmin";
    }

}
