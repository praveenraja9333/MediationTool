package com.vrp.tool.util;

import com.jcraft.jsch.*;
import com.vrp.tool.models.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SFTPUtil {

    private static final String PROTOCOL="sftp";
    private static final Logger LOG= LogManager.getLogger(SFTPUtil.class);

    private SFTPUtil(){}

    public static ChannelSftp getChannelSftp(String username, String remotehost) {
        JSch jSch = getJsch(null);
        Session jSchSession = null;
        try {
            jSchSession = jSch.getSession(username, remotehost);
            jSchSession.setConfig("StrictHostKeyChecking", "no");
            jSchSession.connect(3000);
            return (ChannelSftp) jSchSession.openChannel(PROTOCOL);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }
    public static ChannelSftp getChannelSftp(String username, String remotehost,String privateKey) {
        JSch jSch = getJsch(privateKey);
        Session jSchSession = null;
        try {
            jSchSession = jSch.getSession(username, remotehost);
            jSchSession.setConfig("StrictHostKeyChecking", "no");
            jSchSession.connect();
            return (ChannelSftp) jSchSession.openChannel(PROTOCOL);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChannelSftp getChannelSftp(Job job){
        if(!(job.getKey()==null)&&!"".equals(job.getKey())&&!job.getDstPort().equals("22"))
            return getChannelSftp(job.getUsername(),job.getDstIP(),job.getKey(),Integer.parseInt(job.getDstPort()));
        else if(!(job.getKey()==null)&&!"".equals(job.getKey()))
            return getChannelSftp(job.getUsername(),job.getDstIP(),job.getKey());
        else
            return getChannelSftp(job.getUsername(),job.getDstIP());
    }

    public static ChannelSftp getChannelSftp(String username, String remotehost,int port) {
        JSch jSch = getJsch(null);
        Session jSchSession = null;
        try {
            jSchSession = jSch.getSession(username, remotehost,port);
            jSchSession.setConfig("StrictHostKeyChecking", "no");
            jSchSession.connect();
            return (ChannelSftp) jSchSession.openChannel(PROTOCOL);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }
    public static ChannelSftp getChannelSftp(String username, String remotehost,String privatekey,int port) {
        JSch jSch = getJsch(privatekey);
        Session jSchSession = null;
        try {
            jSchSession = jSch.getSession(username, remotehost,port);
            jSchSession.setConfig("StrictHostKeyChecking", "no");
            jSchSession.connect();
            return (ChannelSftp) jSchSession.openChannel(PROTOCOL);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }


    private static JSch getJsch(String privateKey){
        JSch jSch = new JSch();
        String known_hosts=System.getProperty("knownHosts");
        if(known_hosts==null||"".equals(known_hosts)) {
            String os = System.getProperty("os.name");
            String userhome=System.getProperty("user.home");
            try {
                if (os.contains("Windows")) {
                    String keyFile=(privateKey==null||"".equals(privateKey))?userhome+"\\.ssh\\id_rsa":privateKey;
                    jSch.addIdentity(keyFile);
                    jSch.setKnownHosts("C:\\Users\\Praveen\\.ssh\\known_hosts");
                }
                else if (os.contains("linux")) {
                    String keyFile=(privateKey==null||"".equals(privateKey))?"/.ssh/id_rsa":privateKey;
                    jSch.addIdentity(privateKey);
                    jSch.setKnownHosts("/home/praveen/.ssh/known_hosts");
                }
            }catch (JSchException j ){
                j.printStackTrace();
            }
        }
        return jSch;
    }

    private static String getPrivateKey(String privateKeyFile) throws IOException {
        FileInputStream fin=new FileInputStream(privateKeyFile);
        byte[] bytes=new byte[2048];
        int n=0;
        StringBuilder stringBuilder=new StringBuilder();
        String key="";
        while((n=fin.read(bytes))>-1){
               //stringBuilder.append(Arrays.copyOf(bytes,n));
            key +=new String(Arrays.copyOf(bytes,n));
        }
        return stringBuilder.toString();
    }
}
