package com.ylms.common;

import com.alibaba.fastjson.JSONObject;
import com.ylms.entity.AccessTokenRunTime;
import com.ylms.entity.IpWhite;
import com.ylms.service.IpWhiteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: WeiXinIpServletInit
 * @Description: ip白名单设置
 * @Author: 49524
 * @Date: 2018/8/23 16:41
 * @Version 1.0
 */
//用户注解配置servlet启动时自动加载，必须加value和loadOnStartup属性
//loadOnStartup启动顺序1,2,3数字起小就优先启动
@WebServlet(value = "/WeiXinIpServletInit", loadOnStartup = 1)
public class WeiXinIpServletInit extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(WeiXinIpServletInit.class);
	// 刷新accesstoken
	@Autowired
	private AccessTokenRunTime accesstoken;

	@SuppressWarnings("unchecked")
	@Override
	public void init(ServletConfig config) throws ServletException {
		LOG.info("*******************IP白名单初始化,保证接口的安全！******************");
		ServletContext sc = config.getServletContext();
		if (accesstoken.getRunTime() <= 0 || accesstoken.getRunTime() > 7200) {
			accesstoken.setRunTime(7200L);
		}
		AccessToken.runtime(accesstoken.getRunTime() * 1000L - 20);
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (AccessToken.accessToken.size() > 0) {
				break;
			}
		}
		String accessToken = AccessToken.accessToken.get(0);
		if (!accessToken.equals("") && accessToken != null) {
			final String URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=" + accessToken;
			RestTemplate client = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					null, headers);
			// 执行HTTP请求
			try {
				ResponseEntity<String> resp = client.exchange(URL, HttpMethod.GET, requestEntity, String.class);
				Map<String, Object> local = JSONObject.parseObject(resp.getBody(), Map.class);
				List<String> list = (List<String>) local.get("ip_list");
				// 获取web应用中的spring容器
				ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(sc);
				IpWhiteService ipWhiteService = (IpWhiteService) ac.getBean("ipWhiteService");
				IpWhite ipWhite = ipWhiteService.findAll();
				if (ipWhite == null) {
					String msgIp = list.stream().collect(Collectors.joining(","));
					IpWhite ip = new IpWhite();
					ip.setIpWhite(msgIp);
					ipWhiteService.add(ip);
				} else {
					String msgIp = list.stream().collect(Collectors.joining(","));
					ipWhite.setIpWhite(msgIp);
					ipWhiteService.update(ipWhite);
				}
				LOG.info("*************IP白名单初始化完成*********" + list.toString());
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("发生错误 msg={}", "原因", e);
			}

		} else {
			LOG.info("sorry! whiteIP init failed");
		}
	}
}
