package cn.wzz.actrowdfuding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

//@EnableHystrix			//启用熔断器
@ServletComponentScan	//Servlet组件扫描，扫描监听器、过滤器之类的
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ActrowdfudingCloudPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActrowdfudingCloudPortalApplication.class, args);
	}
}
