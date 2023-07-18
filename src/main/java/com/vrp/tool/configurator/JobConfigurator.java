package com.vrp.tool.configurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrp.tool.models.Job;
import com.vrp.tool.models.JobBuilder;
import com.vrp.tool.models.Node;
import com.vrp.tool.service.JobServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

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

    private static Pattern PATTERN= Pattern.compile(".*\\.json");
    private static Pattern IPPATTERN=Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                                                            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                                                            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                                                            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static Pattern PORTPATTERN=Pattern.compile("^[1-6]?[1-9]{1,5}");

    private static Pattern CRONPATTERN = Pattern.compile("^(\\*|[0-5]?[0-9]|0\\/[0-5]?[0-9]|[0-5]?[0-8](\\-|,)[0-5]?[0-9])\\s" +
            "(\\*|[0-5]?[0-9]|0\\/[0-5]?[0-9]|[0-5]?[0-8](\\-|,)[0-5]?[0-9]|)\\s" +
            "(\\*|[0-1]?[0-9]|2[0-3]|0\\/2[0-3]|0\\/[0-1]?[0-9]|[0-1]?[0-9]\\-[0-1]?[0-9]|[0-2]?[0-3](\\-|,)[0-2]?[0-3]|\\??)\\s" +
            "(\\*|[0-2]?[0-9]|3[0-1]|0\\/3[0-1]|0\\/[0-2]?[0-9]|[0-2]?[0-9]\\-[0-2]?[0-9]|[0-3]?[0-1](\\-|,)[0-3]?[0-1]|\\??)\\s" +
            "(\\*|[a-zA-Z]{3}\\-[a-zA-Z]{3}|[0-7](\\-|,)[0-7]|\\??)\\s" +
            "(\\*|[a-zA-Z]{3}\\-[a-zA-Z]{3}|[0-9](\\-|,)[0-9]|[0-1]?[0-2](\\-|,)[0-1]?[0-2]|\\??)\\s?" +
            "([0-9]{0,4}|\\??)");
    private static int BUFFER_SIZE=65536;


    public void addSubscribers(){
        LOG.info("Subscribed ");
        jobPublisher.addSubscribers(serviceFactory.jobsListener);
        nodePublisher.addSubscribers(serviceFactory.nodesListener);
    }

    public void parseNode(Node node){
           nodePublisher.published(node);
    }

    public void installNode(String fileName){
        Matcher matcher=PATTERN.matcher(fileName);
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

    public void parseJobs(String fileName){
        try (BufferedInputStream br=new BufferedInputStream(new FileInputStream(fileName))){
            String lines="";
            lines=readLines(br);
            int index=-1;
            while(lines.length()>0){
                index=lines.indexOf('\n');
                if(index>0){
                    installJobs(lines.substring(0,index));
                    if(!(lines.length()==(index+1))) lines=lines.substring((index+1));
                    else lines =readLines(br);
                }else{
                    int before=lines.length();
                    lines +=readLines(br);
                    if(before==lines.length()){
                        installJobs(lines);
                        lines="";
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void installJobs(String line){
        LOG.info(line);
        String[] fields=line.trim().split(",");
        String id=requireNonNull(fields[0]);
        String username=requireNonNull(fields[1]);
        String dstIP=requireNonNull(fields[2]);
        String dstPort=requireNonNull(fields[3]);
        String portocol=requireNonNull(fields[4]);
        String nodeNames=requireNonNull(fields[5]);
        String cronexpression=requireNonNull(fields[6]).replace('.',',');


        if(!IPPATTERN.matcher(dstIP).matches()){
            LOG.info("Not a valid IP {}",dstIP);
            return;
        }
        if(!PORTPATTERN.matcher(dstPort).matches()){
            LOG.info("Not a valid Port {}",dstPort);
            return ;
        }
        if(!CRONPATTERN.matcher(cronexpression).matches()){
            LOG.info("Not a valid cronexpression {}"+cronexpression);
            return;
        }
        for(String nodeName:nodeNames.split(":")) {
            if(!serviceFactory.getInstalledNodes().containsKey(nodeName)){
                LOG.info("Node not installed .Please install node %s before configuring job {}",nodeName);
                continue;
            }
            Job job = JobBuilder.newBuilder().setDstIP(dstIP).setDstPort(dstPort).setNode(serviceFactory.getInstalledNodes()
                    .get(nodeName)).setProtocol(portocol).setUserName(username).setCron(cronexpression).setJobid(Integer.parseInt(id)).build();
            jobPublisher.published(job);
        }

    }

    public String readLines(InputStream in) throws IOException {
        byte[] bytes=new byte[BUFFER_SIZE];
        int readcount=in.read(bytes);

        return readcount==-1?"":new String(Arrays.copyOf(bytes,readcount));
    }

    
}
