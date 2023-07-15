package com.vrp.tool.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RunnableJob implements Job {
    private static Logger LOG= LogManager.getLogger(RunnableJob.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
                  LOG.info("Job scheduled and running {}",context.get("jobName"));
    }
}
