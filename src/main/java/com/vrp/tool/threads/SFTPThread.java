package com.vrp.tool.threads;

import com.vrp.tool.models.File;
import com.vrp.tool.models.Job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class SFTPThread extends Thread{
    private static Logger LOG= LogManager.getLogger(SFTPThread.class);
    private volatile Job job;

    private boolean registered=true;

    private volatile Set<File> files;

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
            if(job==null&&!registered){
                register();
            }
            while(job==null){
                try {
                    LOG.info("No Job is scheduled for the Thread {},Hence sleeping",this.getName());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            registered=false;
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
        registered=true;
    }
    public void setFiles(Set<File> files) {
        this.files = files;
    }

}
