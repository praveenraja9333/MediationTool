package com.vrp.tool.admin;

import com.vrp.tool.common.ClientHeaders;
import com.vrp.tool.common.RMIResponse;
import com.vrp.tool.common.RemoteObject;
import com.vrp.tool.common.SessionKey;
import com.vrp.tool.models.Node;
import com.vrp.tool.service.JobServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class AdminRMI extends UnicastRemoteObject implements RemoteObject {
    @Autowired
    private JobServiceFactory jobServiceFactory;

    private int noOfClients=3;

    private static Logger LOG= LogManager.getLogger(UnicastRemoteObject.class);

    private final Map<String,SessionKey> clients=new HashMap<>();

    private volatile boolean exit=false;

    private volatile SessionKey sessionKey=null;

    private Thread monitorLock= null;
    public AdminRMI() throws RemoteException {
           String nofclients=System.getProperty("NoOfRMIClients");
           if(nofclients!=null&&!"".equals(nofclients)){
               noOfClients=Integer.parseInt(nofclients);
           }
    }



    @Override
    public Map<String, Node>printHello() {
        return jobServiceFactory.getInstalledNodes();
    }

    @Override
    public RMIResponse<String> getString(SessionKey sessionKey) throws RemoteException {
        if(validateSessionKey(sessionKey)){
              return new RMIResponse<>("Not Connected to AdminServer ",null,"Success");
        }
        return new RMIResponse<>("Not Connected to AdminServer ",null,"Failed");
    }

    @Override
    public synchronized RMIResponse<SessionKey> register(ClientHeaders clientHeaders) throws RemoteException {
        if(clients.size()<3){
            SessionKey sessionKey=new SessionKey("Token@"+clientHeaders.getIP()+"@"+clientHeaders.getL(),true,clientHeaders.getL());
            Timer timer=new Timer();
            timer.schedule(getTimerTask(sessionKey),10000L);
            clients.put(sessionKey.getToken(),sessionKey);
            return new RMIResponse<>(sessionKey,clientHeaders,"Success");
        }
        String status="Reached Max no of clients "+noOfClients+" Allowed, Please find the active session below";
        for(SessionKey _sessionKey:clients.values()){
            status +=" there is an active session from IP"+(_sessionKey!=null?_sessionKey.getToken().split("@")[1]:null)+
                    " time out remaining "+(300-(((System.currentTimeMillis()- _sessionKey.getLastaccessed()) / 60) / 60))
            +"\n";

        }
        return new RMIResponse<>(null,clientHeaders,status);
    }

    @Override
    public boolean unregister(SessionKey sessionKey) throws RemoteException{
        if(validateSessionKey(sessionKey)){
            this.clients.remove(sessionKey.getToken());
        }
        return false;
    }





    private TimerTask getTimerTask(SessionKey sessionKey) {
        return new TimerTask() {

            @Override
            public void run() {
                while (true) {
                    try {
                        if ((((System.currentTimeMillis() - sessionKey.getLastaccessed()) / 60) / 60) > 300 || sessionKey.isExit()) {
                            break;
                        }
                        LOG.info("Session acquired by the " + sessionKey.getToken());
                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
               clients.remove(sessionKey.getToken());

            }
        };
    }

    private boolean validateSessionKey(SessionKey sessionKey){
        return this.clients.values().contains(sessionKey);
    }

}
