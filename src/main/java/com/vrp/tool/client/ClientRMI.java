package com.vrp.tool.client;

import com.vrp.tool.common.ClientHeaders;
import com.vrp.tool.common.RMIResponse;
import com.vrp.tool.common.RemoteObject;
import com.vrp.tool.common.SessionKey;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

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
                    "3:- exit \n";
            System.out.print(s);
            System.out.print("Choose 1 or 2:");
            int a = 0;
            try {
                a = (System.in.read()) - (int) '0';
                System.in.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (a == 1) installNodes();
            else if  (a == 2);
            else System.out.println( RED+" Please choose the correct value "+RESET);
        }
    }

    public void tryConnect(){
        try {
            remoteObject =(RemoteObject) Naming.lookup(RMIREGISTRY+"/admin");
            clientHeaders=new ClientHeaders(Inet4Address.getLocalHost().toString().trim(),System.currentTimeMillis());
            RMIResponse<SessionKey> response=remoteObject.register(clientHeaders);
            sessionKey=response.getBody();
            updateLastQueriedTime();
            if(sessionKey==null)throw new RuntimeException("Session Failed to connect RMI object"+response.getStatus());
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

    public void installNodes(){
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
                        String confirmation = String.valueOf(Arrays.copyOf(bytes, kk));
                        if ("YES".equalsIgnoreCase(confirmation)) {
                            System.out.println("starting installation");

                        } else if ("NO".equals(confirmation)) {
                            System.out.println("installation aborted");
                            break;
                        } else {
                            System.out.print("Please write only 'YES' or 'NO'");
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void sendInstallation(){
        //send a list files to beinstalled;
    }

    private void updateLastQueriedTime(){
        this.lastqueriedtime=System.currentTimeMillis();
        this.warning=false;
    }
}
