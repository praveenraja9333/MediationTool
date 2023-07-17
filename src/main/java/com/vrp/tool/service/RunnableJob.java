package com.vrp.tool.service;

import com.vrp.tool.models.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Set;

public class RunnableJob implements Job {
    private static Logger LOG= LogManager.getLogger(RunnableJob.class);
    private volatile int sequence=0;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobname = context.getJobDetail().getKey().getName();
        String jobgroup = context.getJobDetail().getKey().getGroup();
        JobDataMap map=context.getJobDetail().getJobDataMap();
        com.vrp.tool.models.Job job = (com.vrp.tool.models.Job) map.get("jobDetail");
        Set<File> collectedfiles= (Set<File>) map.get("alreadyCollected");
        //getFilesFromSFTPserver
        //VerifyAlreadyCollectedFiles
        //Logic to request Threads
        //if no Threads found schedule a fileBack method
        int count=-1;
        //while(++count<100){
            LOG.info("Job scheduled and running {} count {} jobname {} jobNode {} ",context.get("jobName"),count,jobname,job.getNode().getName());
        //}

    }
}
