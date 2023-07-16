package com.vrp.tool.schedular;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.models.Job;
import com.vrp.tool.service.JobServiceFactory;
import com.vrp.tool.service.RunnableJob;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MainSchedular  {

    @Autowired
    private JobServiceFactory jobServiceFactory;
    @Autowired
    private SchedulerFactory schedularFactory;
    private Scheduler schedular;


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
            org.quartz.JobDetail jobDetail = JobBuilder.newJob(RunnableJob.class).withIdentity(job.toString(), "group1")
                    .build();
            jobDetail.getJobDataMap().put("jobDetail", job);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.toString() + ":t", "group1").startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronPattern())).build();
            try {
                schedular.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onRemove(Job job) {

        }

        @Override
        public void onPublish(List<Job> T) {

        }

        @Override
        public void onRemove(List<Job> T) {

        }
    } ;

}
