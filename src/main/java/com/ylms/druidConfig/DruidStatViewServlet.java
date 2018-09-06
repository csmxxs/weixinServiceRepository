package com.ylms.druidConfig;


import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @ClassName: DruidStatViewServlet
 * @Description: 登录durid管理界面的配置
 * @Author: 49524
 * @Date: 2018/8/19 14:50
 * @Version 1.0
 */
@WebServlet(
        urlPatterns= {"/druid/*"},
        initParams= {
                @WebInitParam(name="allow",value="127.0.0.1"),
                @WebInitParam(name="loginUsername",value="root"),
                @WebInitParam(name="loginPassword",value="123456"),
                @WebInitParam(name="resetEnable",value="false")// 不允许HTML页面上的“Reset All”功能
        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;

}
