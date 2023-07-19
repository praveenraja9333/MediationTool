package com.vrp.tool.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.vrp.tool.models.File;
import com.vrp.tool.util.SFTPUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.*;
import java.util.regex.Pattern;

@Configurable
public class RunnableJob implements Job {

    private static Logger LOG= LogManager.getLogger(RunnableJob.class);
    private volatile int sequence=0;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobname = context.getJobDetail().getKey().getName();
        String jobgroup = context.getJobDetail().getKey().getGroup();
        JobDataMap map=context.getJobDetail().getJobDataMap();
        com.vrp.tool.models.Job job = (com.vrp.tool.models.Job) map.get("jobDetail");
        HashSet<File> collectedfiles= (HashSet<File>) map.get("alreadyCollected");
        ChannelSftp channelSftp;
        if(!(job.getKey()==null)&&!"".equals(job.getKey())&&!job.getDstPort().equals("22"))
             channelSftp= SFTPUtil.getChanneSftp(job.getUsername(),job.getDstIP(),job.getKey(),Integer.parseInt(job.getDstPort()));
        else if(!(job.getKey()==null)&&!"".equals(job.getKey()))
            channelSftp= SFTPUtil.getChanneSftp(job.getUsername(),job.getDstIP(),job.getKey());
        else
            channelSftp= SFTPUtil.getChanneSftp(job.getUsername(),job.getDstIP());

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
            channelSftp.ls(job.getNode().getDstDirectory(),lsEntrySelector);
        } catch (SftpException e) {
            throw new RuntimeException(e);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        Iterator<ChannelSftp.LsEntry> e=v.iterator();
        List<File> filteredFiles=new LinkedList<>();
        File fileShell=new File(null,0);


        while(e.hasNext()){
            fileShell.setName(e.next().getFilename());
            if(!collectedfiles.contains(fileShell))filteredFiles.add(new File(e.next().getFilename(),System.currentTimeMillis()));
        }




        //Logic to request Threads
        //if no Threads found schedule a fileBack method
        int count=-1;
        //while(++count<100){
            LOG.info("Job scheduled and running {} count {} jobname {} jobNode {} ",context.get("jobName"),count,jobname,job.getNode().getName());
        //}

    }
}
