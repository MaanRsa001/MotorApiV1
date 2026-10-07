package com.maan.eway.config;

import java.util.concurrent.Executor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfigurationCalc {

	private Logger log = LogManager.getLogger(AsyncConfigurationCalc.class);

	@Bean(name = "calctaskExecuter")
	public Executor taskExecuter() {
		// ThreadPoolTaskExecutor taskExecuter = new ThreadPoolTaskExecutor();

//    taskExecuter.setCorePoolSize(1);      // ONLY ONE THREAD
//    taskExecuter.setMaxPoolSize(1);       // NO PARALLEL EXECUTION
//    taskExecuter.setQueueCapacity(1000);  // Requests wait here
//    taskExecuter.setWaitForTasksToCompleteOnShutdown(true);
//    taskExecuter.setAwaitTerminationSeconds(240);
//    taskExecuter.setThreadNamePrefix("Calc-SINGLE-");

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);

		executor.setQueueCapacity(200);
		executor.setKeepAliveSeconds(120);

		// executor.setCorePoolSize(8);
		// executor.setMaxPoolSize(16);
		// executor.setQueueCapacity(500);
		// executor.setKeepAliveSeconds(120);

		executor.setThreadNamePrefix("Calc-ASYNC-");

		executor.initialize();
		log.info("Calc Async Executor initialized");

		// return taskExecuter;
		return executor;
	}
}
