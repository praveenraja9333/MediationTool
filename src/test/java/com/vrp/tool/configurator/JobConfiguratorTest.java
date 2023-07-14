package com.vrp.tool.configurator;

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
           assertTrue(jobServiceFactory.getInstalledNodes().contains(NodeBuilder.newBuilder().setName("CSSLTE").build()));
    }
}