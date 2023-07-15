package com.vrp.tool.models;

public class JobBuilder implements Builder<JobBuilder>{
    int jobid;
    String dstIP;
    String dstPort;
    String portocol;
    Node node;
    int cycles;

    public JobBuilder setJobid(int jobid) {
        this.jobid = jobid;
        return this;
    }

    public JobBuilder setDstIP(String dstIP) {
        this.dstIP = dstIP;
        return this;
    }

    public JobBuilder setDstPort(String dstPort) {
        this.dstPort = dstPort;
        return this;
    }

    public JobBuilder setPortocol(String portocol) {
        this.portocol = portocol;
        return this;
    }

    public JobBuilder setNode(Node node) {
        this.node = node;
        return this;
    }

    public JobBuilder setCycles(int cycles) {
        this.cycles = cycles;
        return this;
    }

    public static JobBuilder newBuilder(){
        return new JobBuilder();
    }


    @Override
    public Job build(JobBuilder jobBuilder) {
        Job job=new Job();
        job.setJobid(jobBuilder.jobid);
        job.setDstIP(jobBuilder.dstIP);
        job.setDstPort(jobBuilder.dstPort);
        job.setNode(jobBuilder.node);
        job.setPortocol(jobBuilder.portocol);
        job.setCycles(jobBuilder.cycles);
        return job;
    }

    @Override
    public Job build() {
        return build(this);
    }
}
