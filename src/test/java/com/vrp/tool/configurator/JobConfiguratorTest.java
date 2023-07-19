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
        assertEquals(1,jobServiceFactory.getInstalledJobs().size());
        Job job = JobBuilder.newBuilder().setDstIP("192.168.56.11").setDstPort("22").setNode(jobServiceFactory.getInstalledNodes()
                .get("LTECSS3_1")).setProtocol("sftp").setUserName("praveen").setCron("0 0/3 * * * ?").setJobid(1).build();
        assertTrue(jobServiceFactory.getInstalledJobs().contains(job));
    }
    @Test
    void installJobs_cronPatterns(){
        assertEquals(1,jobServiceFactory.getInstalledJobs().size());
        Job job = JobBuilder.newBuilder().setDstIP("192.168.56.11").setDstPort("22").setNode(jobServiceFactory.getInstalledNodes()
                .get("LTECSS3_1")).setProtocol("sftp").setUserName("praveen").setCron("0 0/3 * * * ?").setJobid(1).build();
        assertTrue(jobServiceFactory.getInstalledJobs().contains(job));
    }
}