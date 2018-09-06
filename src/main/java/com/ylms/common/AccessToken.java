package com.ylms.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName: AccessToken
 * @Description: token中控器
 * @Author: 49524
 * @Date: 2018/8/22 11:47
 * @Version 1.0
 */

public class AccessToken {
	private static final Logger LOG = LoggerFactory.getLogger(AccessToken.class);
	private static final String URL = "https://api.weixin.qq.com/cgi-bin/token?";
	private static final String GRANT_TYPE = "client_credential";
	private static final String APPID = "wxaa0a35448c6827bf";
	private static final String SECRET = "a16b55c775a1e9566887f004d87c78b7";
	public static final List<String> accessToken = new ArrayList<>();

	/**
	 * @Author: Xxs
	 * @Description: 获取tonken
	 * @Date: 11:55 2018/8/22
	 * @Param: []
	 * @return: java.lang.String
	 **/
	@SuppressWarnings("unchecked")
	private static void getAccessToken() {
			try {
				final String URL = AccessToken.URL + "&grant_type=" + AccessToken.GRANT_TYPE + "&appid="
						+ AccessToken.APPID + "&secret=" + AccessToken.SECRET;
				RestTemplate client = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				// 请勿轻易改变此提交方式，大部分的情况 下，提交方式都是表单提交
				// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
						null, headers);
				// 执行HTTP请求
				ResponseEntity<String> response = client.exchange(URL, HttpMethod.GET, requestEntity, String.class);
				Map<String, Object> map = JSONObject.parseObject(response.getBody(), Map.class);
				accessToken.clear();
				accessToken.add((String) map.get("access_token"));
			} catch (Exception e) {
				LOG.error("获取微信服务器access_token发生异常 msg={}", "原因", e);
				accessToken.clear();
				accessToken.add(null);
			}
	}
    //定时器
	public static void runtime(Long runTime) {
		// 以下是几种常用调度task的方法：
		// time为Date类型：在指定时间执行一次（不周期）。
		// timer.schedule(task, time);
		// firstTime为Date类型,period为long
		// 从firstTime时刻开始，每隔period毫秒执行一次。
		// timer.schedule(task, firstTime, period);
		// timer.schedule(task, delay) // delay 为long类型：从现在起过delay毫秒之后执行一次（不周期）
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
			public void run() {
					getAccessToken();
				}
			};
			timer.schedule(task, 0L, runTime);// 从现在起，每隔7100秒执行一次

	}
}
