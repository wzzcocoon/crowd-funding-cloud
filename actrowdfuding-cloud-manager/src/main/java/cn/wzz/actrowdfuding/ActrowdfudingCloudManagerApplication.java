package cn.wzz.actrowdfuding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ActrowdfudingCloudManagerApplication {

	/** RestTemplate默认是无法查找注册中心的服务的，需要增加负载均衡注解。*/
	@Bean
	@LoadBalanced	
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ActrowdfudingCloudManagerApplication.class, args);
	}

}
