package com.vrp.tool.models;

public class JobBuilder implements Builder<JobBuilder>{
    int jobid;

    String username;
    String dstIP;
    String dstPort;
    String protocol;
    Node node;
    String cronPattern;
    int cycles;

    public JobBuilder setJobid(int jobid) {
        this.jobid = jobid;
        return this;
    }

    public JobBuilder setUserName(String userName){
        this.username=userName;
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

    public JobBuilder setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }
    public JobBuilder setCron(String cronPattern) {
        this.cronPattern = cronPattern;
        return this;
    }

    public JobBuilder setNode(Node node) {
        this.node = node;
        return this;
    }


    public static JobBuilder newBuilder(){
        return new JobBuilder();
    }


    @Override
    public Job build(JobBuilder jobBuilder) {
        Job job=new Job();
        job.setJobid(jobBuilder.jobid);
        job.setUsername(jobBuilder.username);
        job.setDstIP(jobBuilder.dstIP);
        job.setDstPort(jobBuilder.dstPort);
        job.setNode(jobBuilder.node);
        job.setProtocol(jobBuilder.protocol);
        job.setCronPattern(jobBuilder.cronPattern);
        return job;
    }

    @Override
    public Job build() {
        return build(this);
    }
}
