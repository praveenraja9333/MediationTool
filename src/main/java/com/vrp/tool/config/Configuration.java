package com.vrp.tool.config;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public SchedulerFactory getSchedulerFactory(){
            return new StdSchedulerFactory();
    }



}
