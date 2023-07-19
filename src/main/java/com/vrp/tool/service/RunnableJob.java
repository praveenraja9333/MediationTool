package com.vrp.tool.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vrp.tool.models.File;
import com.vrp.tool.threads.SFTPThread;
import com.vrp.tool.threads.ThreadDispatcher;
import com.vrp.tool.util.SFTPUtil;
import com.vrp.tool.util.ThreadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.vrp.tool.util.FileUtil.getFileSeparators;

@DisallowConcurrentExecution
public class RunnableJob implements Job {

    private static Logger LOG= LogManager.getLogger(RunnableJob.class);
    private  LinkedList<File> files;
    private volatile int sequence=0;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobname = context.getJobDetail().getKey().getName();
        String jobgroup = context.getJobDetail().getKey().getGroup();
        JobDataMap map=context.getJobDetail().getJobDataMap();
        com.vrp.tool.models.Job job = (com.vrp.tool.models.Job) map.get("jobDetail");
        ThreadDispatcher threadDispatcher = (ThreadDispatcher) map.get("threadDispatcher");
        HashSet<File> collectedfiles= (HashSet<File>) map.get("alreadyCollected");
        ChannelSftp channelSftp=SFTPUtil.getChannelSftp(job);
        long startTime=System.currentTimeMillis();
        final Vector<ChannelSftp.LsEntry> v=new Vector<>();
        Pattern pattern;
        String fileregex=job.getNode().getFileregex();
        if(fileregex!=null&&!"".equals(fileregex))
            pattern=Pattern.compile(fileregex);
        else
            pattern=Pattern.compile(".*");
        ChannelSftp.LsEntrySelector lsEntrySelector=(lsentry)->{
            if(pattern.matcher(lsentry.getFilename()).matches())
                v.add(lsentry);
            return 0;
        };

        try {
            channelSftp.connect();
            channelSftp.ls(job.getNode().getSrcDirectory(),lsEntrySelector);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }finally {
            channelSftp.disconnect();
        }

        Iterator<ChannelSftp.LsEntry> e=v.iterator();
        LinkedList<File> filteredFiles=new LinkedList<>();
        File fileShell=new File(null,0);
        String fileName ="";
        while(e.hasNext()) {
             fileName +=e.next().getFilename();
            fileShell.setName(fileName);
            if (!collectedfiles.contains(fileShell))
                filteredFiles.add(new File(fileName, System.currentTimeMillis()));
            fileName="";
        }
        int nooffiles=filteredFiles.size();
        if(nooffiles<1){
            LOG.info("No Files to fetch for job {}",jobname);
            return;
        }
        LOG.debug("{} files to be fetch by job {}",nooffiles,jobname);
        int noOfThreads= ThreadUtil.getThreadCount(nooffiles);


        if(job.getNode().getMaxworkers()>0){
            noOfThreads=job.getNode().getMaxworkers()<noOfThreads?job.getNode().getMaxworkers():noOfThreads;
        }
        List<SFTPThread> threads=threadDispatcher.consume(noOfThreads);

        //List<SFTPThread> threads=threadDispatcher.consume(1);
        int sftpthreadsize=threads.size();
        LOG.debug("Number of Workers Threads {} "+threads.size());
        CountDownLatch countDownLatch=new CountDownLatch(threads.size());
        files=(LinkedList<File>) filteredFiles;
        int filesPerBucket=0;
        for(SFTPThread thread:threads){
            thread.setJob(job);
            thread.setFiles(collectedfiles);
            thread.setJob(this);
            thread.setCountDownLatch(countDownLatch);
        }
        boolean failbackMethod=false;
        if(sftpthreadsize==0&&nooffiles>0){
            LOG.info("No thread were provisioned for the active job {}. Hence using the failback Method",jobname);
              failbackMethod=true;
        }
        if(failbackMethod){
            try {
                channelSftp= SFTPUtil.getChannelSftp(job);
                channelSftp.connect();
                File f;
                int successcount=0;
                int failedccount=0;
                while((f=filteredFiles.poll())!=null){
                    channelSftp.get(job.getNode().getSrcDirectory()+getFileSeparators()+f.getName(),job.getNode().getDstDirectory());
                    f.setId(System.currentTimeMillis());
                    if(this.files.add(f))successcount++;
                    else failedccount++;
                }
                LOG.info("Number of fails collected by the failback method is {}, failed to collect",successcount,failedccount);
            } catch (JSchException j) {
                throw new RuntimeException(j);
            } catch (SftpException s) {
                throw new RuntimeException(s);
            }finally {
                if(channelSftp!=null)channelSftp.disconnect();
            }
        }
        if(!failbackMethod) {
            LOG.debug("Job {} is waiting on worker Threads",jobname);
            try {
                if (job.getNode().getMaxJobwaitTime() > 0) {
                    countDownLatch.await(job.getNode().getMaxJobwaitTime(), TimeUnit.SECONDS);
                } else
                    countDownLatch.await(300, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
        long finishedTime=System.currentTimeMillis()-startTime;
        LOG.info("Job {} is finished, Time elapsed {} seconds",jobname,Math.floorDiv(finishedTime,60));
    }

    public synchronized File getFile(){
           return this.files==null?null:files.poll();
    }

}
