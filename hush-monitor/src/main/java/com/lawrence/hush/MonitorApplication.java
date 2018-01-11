package com.lawrence.hush;

import com.lawrence.hush.annotation.EnableHushTurbineStream;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * hystrix-dashboard和turbine-stream聚合, 监控中心, 注册到eureka提供服务
 */
@EnableAdminServer
@EnableHushTurbineStream
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
public class MonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorApplication.class, args);
	}
}
