package com.ylms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "com.ylms.mapper")
@ServletComponentScan//扫描带@WebServlet注解的类
public class WeixinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixinServiceApplication.class, args);
	}
}
