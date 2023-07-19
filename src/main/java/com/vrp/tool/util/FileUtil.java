package com.vrp.tool.util;

public class FileUtil {

    private FileUtil(){}

    public static String getFileSeparators(){
        String os=System.getProperty("os.name");
        if(os!=null&&os.contains("Win"))
            return "\\";
        else
            return "/";
    }
}
