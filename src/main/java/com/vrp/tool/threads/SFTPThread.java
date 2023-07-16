package com.vrp.tool.threads;

import com.vrp.tool.models.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SFTPThread extends Thread{
    private static Logger LOG= LogManager.getLogger(SFTPThread.class);
    private Job job;
    private ThreadDispatcher.CacheTable cacheTable;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public SFTPThread(String threadName, ThreadDispatcher.CacheTable cacheTable){
        super(threadName);
        this.start();
    }
    @Override
    public void run(){
        //Fetch File Logic
        while(true){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOG.info("The sftp collection logic would be written here, current Thread  Name {}",this.getName());
        }
    }

    public void register(){
        cacheTable.register(this);
    }

}
