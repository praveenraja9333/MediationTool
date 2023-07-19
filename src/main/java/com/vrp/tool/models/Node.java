package com.vrp.tool.models;

import java.util.Objects;

public class Node {
        private  String name;
         String srcDirectory;
         String dstDirectory;

         String fileregex;

         int maxworkers;

         int maxJobwaitTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcDirectory() {
        return srcDirectory;
    }

    public void setSrcDirectory(String srcDirectory) {
        this.srcDirectory = srcDirectory;
    }

    public String getDstDirectory() {
        return dstDirectory;
    }

    public void setDstDirectory(String dstDirectory) {
        this.dstDirectory = dstDirectory;
    }

    public String getFileregex() {
        return fileregex;
    }

    public void setFileregex(String fileregex) {
        this.fileregex = fileregex;
    }

    public int getMaxworkers() {
        return maxworkers;
    }

    public void setMaxworkers(int maxworkers) {
        this.maxworkers = maxworkers;
    }

    public int getMaxJobwaitTime() {
        return maxJobwaitTime;
    }

    public void setMaxJobwaitTime(int maxJobwaitTime) {
        this.maxJobwaitTime = maxJobwaitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    } 
}
