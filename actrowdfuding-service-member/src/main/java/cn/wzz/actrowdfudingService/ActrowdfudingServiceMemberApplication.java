package cn.wzz.actrowdfudingService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("cn.wzz.**.dao")
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ActrowdfudingServiceMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActrowdfudingServiceMemberApplication.class, args);
	}
}
