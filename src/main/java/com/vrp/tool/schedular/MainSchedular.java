package com.vrp.tool.schedular;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.File;
import com.vrp.tool.models.Job;
import com.vrp.tool.service.JobServiceFactory;
import com.vrp.tool.service.RunnableJob;
import com.vrp.tool.threads.ThreadDispatcher;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MainSchedular  {

    @Autowired
    private JobServiceFactory jobServiceFactory;
    @Autowired
    private SchedulerFactory schedularFactory;

    @Autowired
    private ThreadDispatcher threadDispatcher;
    private Scheduler schedular;

    public Map<String, Set<File>>collectedFiles=new ConcurrentHashMap<>();

    private Comparator<File> comparator=(f,ff)->Long.compare( f.getId(),ff.getId());


    @PostConstruct
    public void init_schedule(){

        try {
            jobServiceFactory.subscriber.addSubscribers(joblistener);
            schedular=schedularFactory.getScheduler();
            schedular.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }


    Listener joblistener= new Listener<Job>() {
        @Override
        public void onPublish(Job job) {
            Set<File> set=collectedFiles.getOrDefault(job.toString(),new HashSet<>()) ;
            collectedFiles.put(job.toString(),set);
            org.quartz.JobDetail jobDetail = JobBuilder.newJob(RunnableJob.class).withIdentity(job.toString(), "group1")
                    .build();

            jobDetail.getJobDataMap().put("jobDetail", job);
            jobDetail.getJobDataMap().put("alreadyCollected",set);
            jobDetail.getJobDataMap().put("schedular",schedular);
            jobDetail.getJobDataMap().put("threadDispatcher",threadDispatcher);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.toString() + ":t", "group1").startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronPattern())).build();

            try {
                schedular.scheduleJob(jobDetail, trigger);

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onRemove(Job job) {
            try {
                Set<JobKey> jobkeys=schedular.getJobKeys(GroupMatcher.groupEquals("group1")).stream()
                        .filter(j->j.getName().equals(job.toString())).collect(Collectors.toSet());
                for(JobKey jobkey:jobkeys){
                        schedular.deleteJob(jobkey);
                }

            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onPublish(List<Job> T) {

        }

        @Override
        public void onRemove(List<Job> T) {

        }
    } ;

    public Scheduler getSchedular() {
        return schedular;
    }
}
