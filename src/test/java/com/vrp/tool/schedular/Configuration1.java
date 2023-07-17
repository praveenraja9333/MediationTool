package com.vrp.tool.schedular;


import com.vrp.tool.service.JobServiceFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class Configuration1 {
    @Bean
    public MainSchedular getjobservice(){
        return new MainSchedular();
    }
}
