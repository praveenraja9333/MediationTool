package com.vrp.tool.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RunnableJob implements Job {
    private static Logger LOG= LogManager.getLogger(RunnableJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobname = context.getJobDetail().getKey().getName();
        String jobgroup = context.getJobDetail().getKey().getGroup();
        JobDataMap map=context.getJobDetail().getJobDataMap();
        com.vrp.tool.models.Job job = (com.vrp.tool.models.Job) map.get("jobDetail");
        int count=-1;
        //while(++count<100){
            LOG.info("Job scheduled and running {} count {} jobname {} jobNode {} ",context.get("jobName"),count,jobname,job.getNode().getName());
        //}

    }
}
