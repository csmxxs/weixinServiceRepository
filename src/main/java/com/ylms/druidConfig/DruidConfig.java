package com.ylms.druidConfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName: DruidConfig
 * @Description: 配置druid需要的配置类，引入application.yml文件中以spring.datasource开头的信息，因此需要在application.yml文件中配置相关信息。
 * @Author: 49524
 * @Date: 2018/8/19 14:04
 * @Version 1.0
 */
@Configuration
@ServletComponentScan//用于扫描所有的Servlet、filter、listene
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")//读取配置文件
    public DataSource druidDateSource(){
        return new DruidDataSource();
    }

}
