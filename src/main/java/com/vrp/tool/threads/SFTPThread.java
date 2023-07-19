package com.vrp.tool.threads;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vrp.tool.models.File;
import com.vrp.tool.models.Job;
import com.vrp.tool.service.RunnableJob;
import com.vrp.tool.util.FileUtil;
import com.vrp.tool.util.SFTPUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.vrp.tool.util.FileUtil.getFileSeparators;

public class SFTPThread extends Thread{
    private static Logger LOG= LogManager.getLogger(SFTPThread.class);
    private volatile Job job;

    private volatile RunnableJob runnableJob;

    private volatile CountDownLatch countDownLatch;

    private boolean registered=true;

    private volatile Set<File> files;

    public volatile LinkedList<File> tobeCollected=new LinkedList<>();

    private ThreadDispatcher.CacheTable cacheTable;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public SFTPThread(String threadName, ThreadDispatcher.CacheTable cacheTable){
        super(threadName);
        super.setPriority(Thread.MAX_PRIORITY);
        this.cacheTable=cacheTable;
        this.start();
    }
    @Override
    public void run(){
        //Fetch File Logic
        File f;
        int successcount=0;
        int failedccount=0;
        while(true){
            if(job==null&&!registered){
                register();
            }
            while(job==null){
                try {
                    //LOG.info("No Job is scheduled for the Thread {},Hence sleeping",this.getName());
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            registered=false;
            ChannelSftp channelSftp = null;
            try {
                channelSftp= SFTPUtil.getChannelSftp(job);
                channelSftp.connect();
                while((f=tobeCollected.poll())!=null){
                    channelSftp.get(job.getNode().getSrcDirectory()+"/"+f.getName(),job.getNode().getDstDirectory());
                    f.setId(System.currentTimeMillis());
                    if(this.files.add(f))successcount++;
                    else failedccount++;
                }
            } catch (JSchException e) {
                throw new RuntimeException(e);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            }finally {
                if(this.countDownLatch!=null)this.countDownLatch.countDown();
                if(channelSftp!=null)channelSftp.disconnect();
            }
            LOG.info("Thread {} completed, Files collected  {}  and failed   {} ",this.getName(),successcount,failedccount);
            this.job=null;
            this.files=null;
            failedccount=0;
            successcount=0;

        }
    }

    public void register(){
        cacheTable.register(this);
        registered=true;
    }
    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public void setJob(RunnableJob runnableJob) {
        this.runnableJob = runnableJob;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
