package com.vrp.tool.configurator;

import com.vrp.tool.flow.listeners.Listener;
import com.vrp.tool.flow.publishers.Publisher;
import com.vrp.tool.models.Job;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class JobPublisher implements Publisher<Job> {

    List<Listener> subcribers=new LinkedList<>();
    public boolean addSubscribers(Listener listener){
        if(subcribers.contains(listener))return false;
        return true;
    }

    @Override
    public void published(Job job) {

    }

    @Override
    public void removed(Job job) {

    }

    @Override
    public void removed(List<Job> T) {

    }

    @Override
    public void published(List<Job> T) {

    }
}
