package com.vrp.tool;

import com.vrp.tool.configurator.JobConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private JobConfigurator jobConfigurator;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		   jobConfigurator.addSubscribers();
		 // jobConfigurator.installNode("/Users/praveenrajendran/Downloads/MediationTool/smaple.json");
		  // jobConfigurator.parseJobs("/Users/praveenrajendran/Downloads/MediationTool/sample.txt");
		jobConfigurator.installNode("C:\\Users\\Praveen\\Downloads\\tool\\.tool\\smaple.json");
		jobConfigurator.parseJobs("C:\\Users\\Praveen\\Downloads\\tool\\.tool\\sample.txt");

	}
}
