package com.vrp.tool.configurator;

import com.vrp.tool.models.Node;
import com.vrp.tool.service.JobServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class JobConfigurator {

    @Autowired
    private JobServiceFactory serviceFactory;
    @Autowired
    private JobPublisher jobPublisher;
    @Autowired
    private NodePublisher nodePublisher;
    private static Logger LOG= LogManager.getLogger(JobConfigurator.class);


    public void addSubscribers(){
        LOG.info("Subscribed ");
        jobPublisher.addSubscribers(serviceFactory.jobsListener);
        nodePublisher.addSubscribers(serviceFactory.nodesListener);
    }

    public void parseNode(Node node){
           nodePublisher.published(node);
    }
    
}
