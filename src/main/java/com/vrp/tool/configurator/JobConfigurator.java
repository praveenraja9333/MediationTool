package com.vrp.tool.configurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrp.tool.models.Node;
import com.vrp.tool.service.JobServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static Pattern pattern= Pattern.compile(".*\\.json");


    public void addSubscribers(){
        LOG.info("Subscribed ");
        jobPublisher.addSubscribers(serviceFactory.jobsListener);
        nodePublisher.addSubscribers(serviceFactory.nodesListener);
    }

    public void parseNode(Node node){
           nodePublisher.published(node);
    }

    public void installNode(String fileName){
        Matcher matcher=pattern.matcher(fileName);
        System.out.println(matcher.matches());
        if(!matcher.matches()){
            LOG.info("Provide file name %s is not json file",fileName);
            return;
        }
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            Node node= objectMapper.readValue(new File(fileName),Node.class);
            parseNode(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
