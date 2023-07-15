package com.vrp.tool.configurator;

import com.vrp.tool.models.Job;
import com.vrp.tool.models.JobBuilder;
import com.vrp.tool.models.Node;
import com.vrp.tool.models.NodeBuilder;
import com.vrp.tool.service.JobServiceFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JobConfiguratorTest {
    @Autowired
    JobConfigurator jobConfigurator;
    @Autowired
    JobServiceFactory jobServiceFactory;
    @Test
    void parseNode() {

           jobConfigurator.parseNode(NodeBuilder.newBuilder().setName("CSSLTE").build());
           assertEquals(1,jobServiceFactory.getInstalledNodes().size());
           assertTrue(jobServiceFactory.getInstalledNodes().values().contains(NodeBuilder.newBuilder().setName("CSSLTE").build()));
    }
    @Test
    void installNode(){
        jobConfigurator.installNode("/Users/praveenrajendran/Downloads/MediationTool/smaple.json");
        assertEquals(1,jobServiceFactory.getInstalledNodes().size());
        assertTrue(jobServiceFactory.getInstalledNodes().values().contains(NodeBuilder.newBuilder().setName("LTECSS3_1").build()));
    }


    @Test
    void installJobs(){
        jobConfigurator.installNode("/Users/praveenrajendran/Downloads/MediationTool/smaple.json");
        jobConfigurator.parseJobs("/Users/praveenrajendran/Downloads/MediationTool/sample.txt");
        assertEquals(1,jobServiceFactory.getInstalledJobs().size());
        Job job = JobBuilder.newBuilder().setDstIP("127.0.0.1").setDstPort("1111").setNode(jobServiceFactory.getInstalledNodes()
                .get("LTECSS3_1")).setPortocol("sftp").setJobid(1).build();
        assertTrue(jobServiceFactory.getInstalledJobs().contains(job));

    }
}