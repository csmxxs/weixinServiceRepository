package com.ylms.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: FilterConfig
 * @Description: 自定义过滤器配置
 * @Author: 49524
 * @Date: 2018/8/25 17:16
 * @Version 1.0
 */
@Configuration
public class FilterConfig {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean companyUrlFilterRegister() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		// 过滤器名称
		registration.setName("serviceFilter");
		// 注入过滤器
		registration.setFilter(new ServiceFilter());
		// 拦截规则
		registration.addUrlPatterns("/*");
		// 过滤器顺序,值越小越靠前
		registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
		return registration;
	}
}
