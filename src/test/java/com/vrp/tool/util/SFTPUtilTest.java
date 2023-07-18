package com.vrp.tool.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SFTPUtilTest {

    @Test
    public void getChanneSftp(){
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

}