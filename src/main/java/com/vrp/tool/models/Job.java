package com.vrp.tool.models;

public class Job {
    int jobid;
    String dstIP;
    String dstPort;
    String portocol;
    Node node;

    int cycles;

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
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

    public String getPortocol() {
        return portocol;
    }

    public void setPortocol(String portocol) {
        this.portocol = portocol;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (jobid != job.jobid) return false;
        if (!dstIP.equals(job.dstIP)) return false;
        if (!dstPort.equals(job.dstPort)) return false;
        if (!portocol.equals(job.portocol)) return false;
        return node.equals(job.node);
    }

    @Override
    public int hashCode() {
        int result = jobid;
        result = 31 * result + dstIP.hashCode();
        result = 31 * result + dstPort.hashCode();
        result = 31 * result + portocol.hashCode();
        result = 31 * result + node.hashCode();
        return result;
    }
}
