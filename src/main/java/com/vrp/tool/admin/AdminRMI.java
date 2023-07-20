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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class AdminRMI extends UnicastRemoteObject implements RemoteObject {
    @Autowired
    private JobServiceFactory jobServiceFactory;

    private static Logger LOG= LogManager.getLogger(UnicastRemoteObject.class);

    private volatile long registeredSerialKey=0L;

    private volatile boolean exit=false;

    private volatile SessionKey sessionKey=null;

    private Thread monitorLock= null;
    public AdminRMI() throws RemoteException {
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
    public RMIResponse<SessionKey> register(ClientHeaders clientHeaders) throws RemoteException {
        if(registeredSerialKey==0){
            this.registeredSerialKey=clientHeaders.getL();
            this.monitorLock=getThread();
            this.sessionKey=new SessionKey("Token@"+clientHeaders.getIP()+"@"+clientHeaders.getL(),true);
            this.monitorLock.start();
            return new RMIResponse<>(sessionKey,clientHeaders,"Success");
        }
        return new RMIResponse<>(null,clientHeaders," there is an active session from IP"+(sessionKey!=null?sessionKey.getToken().split("@")[1]:null)+
                " time out remaining "+(300-(((System.currentTimeMillis()-registeredSerialKey) / 60) / 60)));
    }

    @Override
    public boolean unregister(SessionKey sessionKey) throws RemoteException{
        if(validateSessionKey(sessionKey)){
            this.exit=true;
            try {
                this.monitorLock.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                this.sessionKey=null;
            }
            return true;
        }
        return false;
    }

    public boolean getHeartBeat(SessionKey sessionKey) throws RemoteException{
        if(validateSessionKey(sessionKey)){
            registeredSerialKey=System.currentTimeMillis();
            return true;
        }
        return false;

    }

    private Thread getThread() {
        return new Thread(()->{

            while(true) {
                try {
                    if ((((System.currentTimeMillis()-this.registeredSerialKey) / 60) / 60) > 300||exit) {
                        exit=false;
                        break;
                    }
                    LOG.info("Session acquired by the "+this.sessionKey.getToken());
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (Long.compare(this.registeredSerialKey, 0)!=0) {
                this.registeredSerialKey = 0L;
            }
        });
    }

    private boolean validateSessionKey(SessionKey sessionKey){
        return this.sessionKey!=null&&this.sessionKey.equals(sessionKey);
    }

}
