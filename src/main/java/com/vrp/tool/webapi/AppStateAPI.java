package com.vrp.tool.webapi;

import com.vrp.tool.models.Job;
import com.vrp.tool.models.Node;
import com.vrp.tool.schedular.MainSchedular;
import com.vrp.tool.service.JobServiceFactory;
import lombok.Data;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppStateAPI {
    @Autowired
    private JobServiceFactory jobServiceFactory;
    @Autowired
    private MainSchedular mainSchedular;

    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY:mm:dd HH:MM:SSZ");

    @GetMapping(value = "/api/v1/getInstalledNodes" ,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Node>> getInstalledNodes(){
           return new ResponseEntity<>(jobServiceFactory.getInstalledNodes().values().stream().collect(Collectors.toList()),HttpStatus.OK);
    }
    @GetMapping(value = "/api/v1/getInstalledJobs" ,produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<List<Job>> getInstalledJobs(){
        return new ResponseEntity<List<Job>>(jobServiceFactory.getInstalledJobs().stream().collect(Collectors.toList()),HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/getScheduledJobs" ,produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Schedules>> getScheduledJobs(){
        Scheduler scheduler=mainSchedular.getSchedular();
        List<Schedules> list=new LinkedList<>();
        try {
            for (String group : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                    String jobname = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger : triggers) {
                        Schedules _s=new Schedules();
                        _s.setJobName(jobname);
                        _s.setGroupName(jobGroup);
                        _s.setNextFireTime(simpleDateFormat.format(trigger.getNextFireTime()));
                        list.add(_s);
                    }
                }
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(code=HttpStatus.METHOD_NOT_ALLOWED,reason = "Post Method Not Allowed")
    public void postBann(){
    }
    @PutMapping
    @ResponseStatus(code=HttpStatus.METHOD_NOT_ALLOWED,reason = "Put Method Not Allowed")
    public void putBann(){
    }
    @DeleteMapping
    @ResponseStatus(code=HttpStatus.METHOD_NOT_ALLOWED,reason = "delete Method Not Allowed")
    public void deleteBann(){
    }

    @Data
    class Schedules{
        String jobName;
        String groupName;
        String nextFireTime;
    }

}
