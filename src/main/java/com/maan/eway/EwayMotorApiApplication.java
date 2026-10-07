package com.maan.eway;

import java.util.concurrent.Executor;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.gson.Gson;


@SpringBootApplication
@EnableBatchProcessing
@EnableTransactionManagement
@EnableAsync
public class EwayMotorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EwayMotorApiApplication.class, args);
	}

	
	@Bean(name = "RegulatoryRequestsExecutor")
	public Executor regulatoryRequestsExecutor() {
		ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("TIRA_REQ");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}	
	
	@Bean(name = "RegulatoryResponseUpdate")
	public Executor RegulatoryResponseExecutor() {
		ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(5);
		executor.setThreadNamePrefix("TIRA_UPD");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}	
	
	@Bean(name = "ONETIME_TABLE_INSERT")
	public Executor oneTimeTableInsert() {
		ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(40);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("ONETIME_INSERT");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}

	@Bean(name = "MotorLookupExecutor")
	public Executor motorLookupExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(16);
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("MOTOR_LOOKUP-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(30);
		executor.initialize();
		return executor;
	}
	
	@Bean
	public Gson initializeGson() {
		return new Gson();
	}
	
}
