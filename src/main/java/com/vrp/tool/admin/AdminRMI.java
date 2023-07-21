package com.vrp.tool.admin;

import com.vrp.tool.common.*;
import com.vrp.tool.configurator.JobConfigurator;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import com.vrp.tool.schedular.MainSchedular;
import com.vrp.tool.service.JobServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AdminRMI extends UnicastRemoteObject implements RemoteObject {
    @Autowired
    private JobServiceFactory jobServiceFactory;

    @Autowired
    private MainSchedular mainSchedular;

    @Autowired
    private JobConfigurator jobConfigurator;

    private int noOfClients=3;

    private static Logger LOG= LogManager.getLogger(UnicastRemoteObject.class);

    private final RMIResponse<Object> staticRMIResponse=new RMIResponse<>();

    private List<String> staticListResponseBody=new LinkedList<>();

    private final Map<String,SessionKey> clients=new HashMap<>();
    private final Map<String,RMIResponse> clientsResponsePool=new HashMap<>();

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
    public synchronized RMIResponse<Object> register(ClientHeaders clientHeaders) throws RemoteException {
        if(clients.size()<3){
            SessionKey sessionKey=new SessionKey("Token@"+clientHeaders.getIP()+"@"+clientHeaders.getL(),true,clientHeaders.getL());
            Timer timer=new Timer();
            timer.schedule(getTimerTask(sessionKey),10000L);
            clients.put(sessionKey.getToken(),sessionKey);
            RMIResponse<Object> rmiResponse=new RMIResponse<>(sessionKey,clientHeaders,"Success");
            clientsResponsePool.put(sessionKey.getToken(),rmiResponse);
            return rmiResponse;
        }
        String status="Reached Max no of clients "+noOfClients+" Allowed, Please find the active session below";
        for(SessionKey _sessionKey:clients.values()){
            status +=" there is an active session from IP"+(_sessionKey!=null?_sessionKey.getToken().split("@")[1]:null)+
                    " time out remaining "+(300-(((System.currentTimeMillis()- _sessionKey.getLastaccessed()) / 60) / 60))
            +"\n";

        }
        staticRMIResponse.setStatus(status);
        return staticRMIResponse;
    }

    @Override
    public boolean unregister(SessionKey sessionKey) throws RemoteException{
        if(validateSessionKey(sessionKey)){
            this.clients.remove(sessionKey.getToken());
        }
        return false;
    }

    @Override
    public synchronized RMIResponse<Object> getScheduledJobs(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            Scheduler scheduler = mainSchedular.getSchedular();
            staticListResponseBody.clear();
            try {
                for (String group : scheduler.getJobGroupNames()) {
                    for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                        String jobname = jobKey.getName();
                        String jobGroup = jobKey.getGroup();
                        List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                        for (Trigger trigger : triggers) {
                            staticListResponseBody.add("[jobName] : " + jobname + " [groupName] : "
                                    + jobGroup + " - " + trigger.getNextFireTime());
                        }
                    }
                }
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
            RMIResponse response = clientsResponsePool.get(rmiRequest.getSessionKey().getToken());
            response.setBody(staticListResponseBody);
            return response;
        }
         return sendInvalidResponse();
    }

    @Override
    public RMIResponse<Object> getInstalledNodes(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            staticListResponseBody.clear();
            RMIResponse response = clientsResponsePool.get(rmiRequest.getSessionKey().getToken());
            response.setBody(jobServiceFactory.getInstalledNodes().values().stream().collect(Collectors.toList()));
            return response;
        }
        return sendInvalidResponse();
    }

    @Override
    public RMIResponse<Object> getInstalledJobs(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            staticListResponseBody.clear();
            RMIResponse response = clientsResponsePool.get(rmiRequest.getSessionKey().getToken());
            response.setBody(jobServiceFactory.getInstalledJobs());
            return response;
        }
        return sendInvalidResponse();
    }

    @Override
    public synchronized  RMIResponse<Object> installNodes(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            if(rmiRequest.getBody()==null){
                  return sendInvalidResponse("Empty Body");
            }
            if(rmiRequest.getBody() instanceof File){
                   jobConfigurator.installNode(((File)rmiRequest.getBody()).getAbsolutePath());
                    return sendInvalidResponse("Success");
            }else if(rmiRequest.getBody() instanceof List<?>){
                  for(File file:(List<File>)rmiRequest.getBody()){
                      jobConfigurator.installNode(((File)file).getAbsolutePath());
                  }
                return sendInvalidResponse("Success");
            }else{
                return sendInvalidResponse("Not a valid object type");
            }
        }
        return sendInvalidResponse();
    }

    @Override
    public RMIResponse<Object> removeNodes(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            if (rmiRequest.getBody() == null) {
                return sendInvalidResponse("Empty Body");
            }
            if(rmiRequest.getBody() instanceof Node){
                jobConfigurator.removeNode((Node)rmiRequest.getBody());
            }
            staticRMIResponse.setStatus("Success");
            return staticRMIResponse;
        }
        return sendInvalidResponse();
    }

    @Override
    public RMIResponse<Object> removeJobs(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            if (rmiRequest.getBody() == null) {
                return sendInvalidResponse("Empty Body");
            }
            if(rmiRequest.getBody() instanceof Job){
                jobConfigurator.removeJob((Job) rmiRequest.getBody() );
            }
            staticRMIResponse.setStatus("Success");
            return staticRMIResponse;
        }
        return sendInvalidResponse();
    }

    @Override
    public RMIResponse<Object> reInstallJobs(RMIRequest rmiRequest) throws RemoteException {
        if (validateSessionKey(rmiRequest.getSessionKey())) {
            jobConfigurator.reinstallJobs();
            staticRMIResponse.setStatus("Success");
            return staticRMIResponse;
        }
        return sendInvalidResponse();
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
        if(this.clients.values().contains(sessionKey)){
           this.clients.get(sessionKey.getToken()).setLastaccessed(System.currentTimeMillis());
           return true;
        }
        return false;
    }

    private RMIResponse<Object> sendInvalidResponse(String ...args){
        if(args==null||args.length==0) {
            staticRMIResponse.setStatus("Failed Invalid Session");
        }else{
            staticRMIResponse.setStatus(Arrays.stream(args).collect(Collectors.joining()));
        }
        return staticRMIResponse;
    }

}
