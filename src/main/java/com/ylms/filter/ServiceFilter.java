package com.ylms.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ylms.entity.IpWhite;
import com.ylms.service.IpWhiteService;
import com.ylms.utils.IPUtils;
import com.ylms.utils.SpringContext;
import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ServiceFilter
 * @Description: 辨识访问来源
 * @Author: 49524
 * @Date: 2018/8/23 9:18
 * @Version 1.0
 */
public class ServiceFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceFilter.class);
	// 存放白名单
	private final List<String> list = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * @Author: Xxs
	 * @Description: 过滤访问源
	 * @Date: 17:19 2018/8/25
	 * @Param: [request, response, chain]
	 * @return: void
	 **/
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String ip = IPUtils.getRealIP(req);
		LOG.info("访问机器的原始IP={}", ip);
		if (isMatchWhiteList(ip)) {
			LOG.info("IP符合规定,开始进入应用业务系统!");
			chain.doFilter(request, response);
			return;
		}
		LOG.info("访问的机器IP=" + ip + "不符合白名单列表,程序不处理!");
	}

	/**
	 * 匹配是否是白名单
	 * 
	 * @param ip
	 * @return
	 */
	private boolean isMatchWhiteList(String ip) {
		// 获取web应用中的spring容器
		if (this.list.size() <= 0) {
			IpWhiteService ipWhiteService = (IpWhiteService) SpringContext.getApplicationContext()
					.getBean("ipWhiteService");
			IpWhite ipWhite = ipWhiteService.findAll();
			String ipArray = ipWhite.getIpWhite();
			String[] array = ipArray.split(",");
			for (String ips : array) {
				list.add(ips);
			}
		}
		LOG.info("正在校验IP地址,请稍后.....");
		if (this.list.contains(ip)) {
			return true;
		}
		return list.stream().anyMatch(data -> ip.startsWith(data));
	}

	@Override
	public void destroy() {

	}
}
