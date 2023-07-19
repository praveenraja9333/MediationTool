package com.vrp.tool.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class SFTPUtilTest {

    @Test
    public void getChanneSftp_userNameAndremote(){
        ChannelSftp channelSftp=SFTPUtil.getChanneSftp("praveen","192.168.56.11");
        Vector<Object> v= null;
        try {
            channelSftp.connect();
             v = channelSftp.ls("/home/praveen/NodeFiles/LTE3_1");
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        assertEquals(12,v.size());
    }
    public void getChanneSftp_key(){
        ChannelSftp channelSftp=SFTPUtil.getChanneSftp("praveen","192.168.56.11","C:\\Users\\Praveen\\jsch\\id_rsa");
        Vector<Object> v= null;
        try {
            channelSftp.connect();
            v = channelSftp.ls("/home/praveen/NodeFiles/LTE3_1");
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        assertEquals(12,v.size());
    }
    @Test
    public void getChanneSftp_port(){
        ChannelSftp channelSftp=SFTPUtil.getChanneSftp("praveen","192.168.56.11",22);
        Vector<Object> v= null;
        try {
            channelSftp.connect();
            v = channelSftp.ls("/home/praveen/NodeFiles/LTE3_1");
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        assertEquals(12,v.size());
    }

    @Test
    public void getChanneSftp_portandkey(){
        ChannelSftp channelSftp=SFTPUtil.getChanneSftp("praveen","192.168.56.11","C:\\Users\\Praveen\\jsch\\id_rsa",22);
        Vector<Object> v= null;
        try {
            channelSftp.connect();
            v = channelSftp.ls("/home/praveen/NodeFiles/LTE3_1");
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        assertEquals(12,v.size());
    }

    @Test
    public void getChanneSftp_portandkey_withselector(){
        ChannelSftp channelSftp=SFTPUtil.getChanneSftp("praveen","192.168.56.11","C:\\Users\\Praveen\\jsch\\id_rsa",22);
        //Vector<Object> v= null;
        Pattern pattern= Pattern.compile(".*\\.txt");
        final Vector<ChannelSftp.LsEntry> v=new Vector<>();
        ChannelSftp.LsEntrySelector lsEntrySelector=(lsentry)->{
            if(pattern.matcher(lsentry.getFilename()).matches())
                v.add(lsentry);
            return 0;
        };
        try {
            channelSftp.connect();
            channelSftp.ls("/home/praveen/NodeFiles/LTE3_1",lsEntrySelector);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        assertEquals(10,v.size());
    }

}