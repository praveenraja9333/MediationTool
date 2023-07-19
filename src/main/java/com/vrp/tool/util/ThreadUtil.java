package com.vrp.tool.util;

public class ThreadUtil {

    private static final int THREADPERFILES=5;

    private ThreadUtil(){}

    public static int getThreadCount(int totalFiles){
        //default Logic to provide single SFTPThread for every 5 files
        String threadperfiles=System.getProperty("threadperfiles");
        int t=0;
        if(threadperfiles!=null&&!"".equals(threadperfiles)){
             t=totalFiles/Integer.parseInt(threadperfiles);
             return t<1?1:t;
        }
        t=totalFiles/THREADPERFILES;
        return t<1?1:t;
    }
}
