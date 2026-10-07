package com.maan.eway.config;

import org.springframework.context.annotation.Configuration;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import jakarta.annotation.PostConstruct;

@Configuration
public class MySQLCleanupConfig {

//	@Bean(destroyMethod = "shutdownCleanup")
//	public Object mysqlCleanup() {
//		return new Object() {
//			public void shutdownCleanup() {
//				try {
//					AbandonedConnectionCleanupThread.checkedShutdown();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//	}

	@PostConstruct
	public void init() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				AbandonedConnectionCleanupThread.checkedShutdown();
				System.out.println("MySQL AbandonedConnectionCleanupThread shutdown completed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}
}
