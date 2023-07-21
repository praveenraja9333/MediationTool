package com.vrp.tool.models;

import java.io.Serializable;

public class Job implements Serializable {
    int jobid;
    String username;
    String dstIP;
    String dstPort;
    String protocol;
    Node node;

    String cronPattern;

    String key;

    public String getCronPattern() {
        return cronPattern;
    }

    public void setCronPattern(String cronPattern) {
        this.cronPattern = cronPattern;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public String getDstIP() {
        return dstIP;
    }

    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }

    public String getDstPort() {
        return dstPort;
    }

    public void setDstPort(String dstPort) {
        this.dstPort = dstPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString(){
        return jobid+ ":"+
                dstIP+":"+dstPort+":"+node.getName()+":"+cronPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (jobid != job.jobid) return false;
        if (!dstIP.equals(job.dstIP)) return false;
        if (!dstPort.equals(job.dstPort)) return false;
        if (!protocol.equals(job.protocol)) return false;
        if (!node.equals(job.node)) return false;
        if (!username.equals(job.username)) return false;
        return cronPattern.equals(job.cronPattern);
    }

    @Override
    public int hashCode() {
        int result = jobid;
        result = 31 * result + dstIP.hashCode();
        result = 31 * result + dstPort.hashCode();
        result = 31 * result + protocol.hashCode();
        result = 31 * result + node.hashCode();
        result = 31 * result + cronPattern.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
