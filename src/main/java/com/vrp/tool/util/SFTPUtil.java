package com.vrp.tool.util;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SFTPUtil {

    private static final String PROTOCOL="sftp";
    private static final Logger LOG= LogManager.getLogger(SFTPUtil.class);

    private SFTPUtil(){}

    public static ChannelSftp getChanneSftp(String username, String remotehost) {
        JSch jSch = new JSch();
        String known_hosts=System.getProperty("knownHosts");
        if(known_hosts==null||"".equals(known_hosts)) {
            String os = System.getProperty("os.name");
            try {
                if (os.contains("Windows")) {
                   // String privatekey=getPrivateKey("C:\\Users\\Praveen\\.ssh\\id_rsa");
                    jSch.addIdentity("C:\\Users\\Praveen\\jsch\\id_rsa");
                    jSch.setKnownHosts("C:\\Users\\Praveen\\.ssh\\known_hosts");
                }
                else if (os.contains("linux")) {
                    jSch.setKnownHosts("/home/praveen/.ssh/known_hosts");
                }
            }catch (JSchException j ){
                j.printStackTrace();
            }
        }
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
    public static ChannelSftp getChanneSftp(String username, String remotehost,String privateKey) {
        JSch jSch = new JSch();
        String known_hosts=System.getProperty("knownHosts");
        if(known_hosts==null||"".equals(known_hosts)) {
            String os = System.getProperty("os.name");
            try {
                if (os.contains("Windows"))
                    jSch.setKnownHosts("C:\\Users\\Praveen\\.ssh\\known_hosts");
                else if (os.contains("linux")) {
                    jSch.setKnownHosts("/home/praveen//.ssh/known_hosts");
                }
            }catch (JSchException j){
                j.printStackTrace();
            }
        }
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
