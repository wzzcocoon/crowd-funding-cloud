package cn.wzz.actrowdfuding.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**接口注入。得到流程框架的配置对象。设置流程框架的默认字体和邮箱端口号*/
@Configuration
public class ActConfig implements ProcessEngineConfigurationConfigurer{

	@Override
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		processEngineConfiguration.setActivityFontName("宋体");
		processEngineConfiguration.setLabelFontName("宋体");
		processEngineConfiguration.setMailServerPort(25);
	}

}
