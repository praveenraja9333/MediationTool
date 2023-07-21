package com.vrp.tool.client;

import com.vrp.tool.common.*;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.vrp.tool.client.ClientRMI.TEXT_COLOR.*;

public class ClientRMI {

    enum TEXT_COLOR{
         RED("\033[0;31m"),YELLOW("\u001B[33m"),RESET("\033[0m");
         private String code;
         TEXT_COLOR(String code){
            this.code=code;
        }
        @Override
        public String toString(){
             return this.code;
        }
    }

    protected volatile long lastqueriedtime=0L;

    protected volatile boolean warning=false;

    private volatile RMIRequest<Object> rmiRequest;

    Thread heartBeat=null;
    TimerTask idletimertask=new TimerTask() {
        @Override
        public void run() {
            while(true){
                if((((System.currentTimeMillis()-lastqueriedtime)/60)/60) > 280){
                    System.out.println(RED+" your session  expired due to inactivity "+RESET);
                    System.exit(2);
                }
                if((((System.currentTimeMillis()-lastqueriedtime)/60)/60) > 200&&!warning){
                    warning=true;
                    System.out.println(YELLOW+" Soon your session will expire due to inactivity "+RESET);
                }
            }
        }
    };
    private RemoteObject remoteObject;

    private final String RMIREGISTRY = "rmi://localhost:2001";

    private SessionKey sessionKey=null;
    private ClientHeaders clientHeaders=null;

    private static byte[] discardbytes=new byte[4098];

    private static final Pattern PATTERN=Pattern.compile(".*\\.json");

    public static void main(String[] args) {
        ClientRMI clientRMI=new ClientRMI();
        clientRMI.showOptions();
    }

    public void showOptions(){
       tryConnect();
       new Timer().schedule(idletimertask,1000L);
        while (true) {
            String s = "This client should  run in localhost to server only \n" +
                    "1:- install Nodes \n" +
                    "2:- Show scheduled jobs \n"+
                    "3:- Show installed Nodes \n"+
                    "4:- Show installed Jobs \n"+
                    "5:- Remove Node( This will impact the current job schedule) \n"+
                    "6:- Remove Job ( This will impact the current job schedule) \n"+
                    "7:- reLoadJob ( This will impact the current job schedule) \n"+
                    "0:- exit \n";
            System.out.print(s);
            System.out.print("Choose 1 or 2:");
            int a = 0;
            try {
                a = (System.in.read()) - (int) '0';
                System.in.read(discardbytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (a == 1) installNodes();
            else if  (a == 2) getScheduledJobs();
            else if  (a == 3) getInstalledNodes();
            else if  (a == 4) getInstalledJobs();
            else if  (a == 5) removeNodes();
            else if  (a == 6) removeJobs();
            else if  (a == 7) reloadJobs();
            else if  (a == 0) System.exit(2);
            else System.out.println( RED+" Please choose the correct value "+RESET);
        }
    }

    public void tryConnect(){
        try {
            remoteObject =(RemoteObject) Naming.lookup(RMIREGISTRY+"/admin");
            clientHeaders=new ClientHeaders(Inet4Address.getLocalHost().toString().trim(),System.currentTimeMillis());
            RMIResponse<Object> response=remoteObject.register(clientHeaders);
            sessionKey=(SessionKey) response.getBody();
            updateLastQueriedTime();
            if(sessionKey==null) {System.out.println("Session Failed to connect RMI object"+response.getStatus());System.exit(200);}
            rmiRequest=new RMIRequest<>(sessionKey);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
    private void getScheduledJobs() {
        try {
            RMIResponse<Object> rmiResponse=remoteObject.getScheduledJobs(rmiRequest);
            if(rmiResponse.getBody()!=null){
                for(String s:(List<String>) rmiResponse.getBody()){
                    System.out.println(s);
                }
            }else{
                System.out.println(rmiResponse.getStatus());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void getInstalledNodes(){
        try {
            RMIResponse<Object> rmiResponse=remoteObject.getInstalledNodes(rmiRequest);
            if(rmiResponse.getBody()!=null){
                for(Node node:(Collection<Node>) rmiResponse.getBody()){
                    System.out.println("[Info] "+node.toString());
                }
            }else{
                System.out.println(rmiResponse.getStatus());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void getInstalledJobs(){
        try {
            RMIResponse<Object> rmiResponse=remoteObject.getInstalledJobs(rmiRequest);
            if(rmiResponse.getBody()!=null){
                for(Job job:(Set<Job>) rmiResponse.getBody()){
                    System.out.println("[Job installed name ] "+job.toString());
                }
            }else{
                System.out.println(rmiResponse.getStatus());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void installNodes(){
        while(true){
            String s="Please provide the JSON file or the directory or type exit :";
            System.out.print(s);
            byte[] bytes=new byte[4098];
            File file;
            String fileName="";
            int n;
            File[] files=null;
            try {
                while((n=System.in.read(bytes))>-1){
                     fileName +=new String(Arrays.copyOf(bytes,n));
                     if(fileName.charAt(fileName.length()-1)=='\n')break;
                }
                fileName=fileName.replace("\n","");
                System.out.println("FileName "+fileName);
                if("".equals(fileName)||"exit".equalsIgnoreCase(fileName))break;
                file=new File(fileName);
                if(file.exists()&&file.isDirectory()){
                    System.out.println("Provided value is directory");
                    files=file.listFiles((d,fn)->PATTERN.matcher(fn).matches());
                }
                if(files!=null&&files.length>0) {
                    System.out.println("Files Choosen for the installation is :");
                    for (File _file : files) {
                        System.out.println(YELLOW +"FileName " + _file.getName()+RESET);
                    }
                    while(true) {
                        System.out.println("do you want continue installtion (YES/NO)");
                        int kk = System.in.read(bytes);
                        String confirmation = new String(Arrays.copyOf(bytes,kk));
                        confirmation=confirmation.replace("\n","");
                        if ("YES".equalsIgnoreCase(confirmation)) {
                            System.out.println("starting installation");
                            sendInstallation(Arrays.stream(files).collect(Collectors.toList()));
                            break;
                        } else if ("NO".equals(confirmation)) {
                            System.out.println("installation aborted");
                            break;
                        } else {
                            System.out.print("Please write only 'YES' or 'NO'");
                        }
                    }
                }
                if(file.exists()&&file.isFile()){
                        sendInstallation(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void removeNodes(){
        try {
            RMIResponse<Object> rmiResponse=remoteObject.getInstalledNodes(rmiRequest);
            if(rmiResponse.getBody()!=null){
                int count=0;
                System.out.println(YELLOW+" Please choose the node to be Removed "+RESET);
                if(((Collection<Node>)rmiResponse.getBody()).size()>0) {
                    for (Node node : (Collection<Node>) rmiResponse.getBody()) {
                        System.out.println((++count) + ":- " + node.getName());
                    }
                    System.out.println(YELLOW +"Please enter the corresponding number of the Node be deleted, Example( 1 or 2)" + RESET);

                    try {
                        int a=System.in.read(discardbytes);
                        int value= NumberFormat.getNumberInstance().parse(new String(Arrays.copyOf(discardbytes,a)).replace("\n","")).intValue();
                        if(value>0&&value<=((Collection<Node>)rmiResponse.getBody()).size()){
                            rmiRequest.setBody(((List<Node>)rmiResponse.getBody()).get(value-1));
                            rmiResponse=remoteObject.removeNodes(rmiRequest);
                            updateLastQueriedTime();
                            System.out.println(rmiResponse.getStatus());
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println(YELLOW+" No Nodes installed "+RESET);
                }
            }else{
                System.out.println(rmiResponse.getStatus());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    private void removeJobs(){
        try {
            RMIResponse<Object> rmiResponse=remoteObject.getInstalledJobs(rmiRequest);
            if(rmiResponse.getBody()!=null){
                int count=0;
                System.out.println(YELLOW+" Please choose the Job to Removed "+RESET);
                if(((Set<Job>)rmiResponse.getBody()).size()>0) {
                    for (Job job : (Set<Job>) rmiResponse.getBody()) {
                        System.out.println((++count) + ":-  " + job);
                    }
                    System.out.println(YELLOW +"Please enter the corresponding number of the Job be deleted, Example( 1 or 2)" + RESET);

                    try {
                        int a=System.in.read(discardbytes);
                        int value= NumberFormat.getNumberInstance().parse(new String(Arrays.copyOf(discardbytes,a)).replace("\n","")).intValue();
                        if(value>0&&value<=((Set<Job>)rmiResponse.getBody()).size()){
                            rmiRequest.setBody(((Set<Job>)rmiResponse.getBody()).stream().collect(Collectors.toList()).get(value-1));
                            remoteObject.removeJobs(rmiRequest);
                            rmiResponse=remoteObject.removeNodes(rmiRequest);
                            updateLastQueriedTime();
                            System.out.println(rmiResponse.getStatus());
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    System.out.println(YELLOW+" No Jobs to scheduled "+RESET);
                }
            }else{
                System.out.println(rmiResponse.getStatus());
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void reloadJobs(){
        try {
            remoteObject.reInstallJobs(rmiRequest);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendInstallation(Object o) {
        RMIResponse rmiResponse = null;
        try {
            if (o instanceof File || o instanceof List<?>) {
                rmiRequest.setBody(o);
                rmiResponse = remoteObject.installNodes(rmiRequest);
                updateLastQueriedTime();
                System.out.println(rmiResponse.getStatus());
            } else {
                System.out.println("Not a valid object type");
            }
        } catch (RemoteException r) {
            throw new RuntimeException(r);
        } finally {

        }
    }

    private void updateLastQueriedTime(){
        this.lastqueriedtime=System.currentTimeMillis();
        this.warning=false;
    }
}
