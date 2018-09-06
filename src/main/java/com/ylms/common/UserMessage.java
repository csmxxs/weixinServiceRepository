package com.ylms.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: UserMessage
 * @Description: 获取用户基本信息
 * @Author: 49524
 * @Date: 2018/8/23 12:21
 * @Version 1.0
 */

public class UserMessage {
	private static final Logger LOG = LoggerFactory.getLogger(UserMessage.class);
	private static final String URL = "https://api.weixin.qq.com/cgi-bin/user/info?";
	private static final String URLS = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?";

	/**
	 * @Author: Xxs
	 * @Description: 获取单个用户信息
	 * @Date: 15:06 2018/8/23
	 * @Param: [openid]
	 * @return: java.lang.String
	 **/
	@SuppressWarnings("unchecked")
	public static String getUserMessage(String openid) {
		try {
			int i = 0;
			String accessToken = null;
			accessToken = AccessToken.accessToken.get(0);
			if (accessToken == null && i < 3) {
				Thread.sleep(1000);
				// 尝试3次请求
				getUserMessage(openid);
				i++;
			}
			if (openid.equals("") || openid == null || accessToken == null) {
				return null;
			}
			final String URL = UserMessage.URL + "&access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
			RestTemplate client = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			// 请勿轻易改变此提交方式，大部分的情况 下，提交方式都是表单提交
			// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					null, headers);
			// 执行HTTP请求
			ResponseEntity<String> response = client.exchange(URL, HttpMethod.GET, requestEntity, String.class);
			Map<String, Object> map = JSONObject.parseObject(response.getBody(), Map.class);
			if (map.containsKey("errcode")) {
				LOG.error("发生异常 msg={}", "原因", response.getBody());
				return "errcode";
			}
			return response.getBody();
		} catch (Exception e) {
			LOG.error("发生异常 msg={}", "原因", e);
			return null;
		}
	}

	/**
	 * @Author: Xxs
	 * @Description: 一次性拉取多个用户信息
	 * @Date: 15:10 2018/8/23
	 * @Param: []
	 * @return: java.lang.String
	 **/
	@SuppressWarnings("unchecked")
	public static String getUserList() {
		String accessToken = AccessToken.accessToken.get(0);
		if (accessToken.equals("errcode") || accessToken == null) {
			return null;
		}
		final String URL = UserMessage.URLS + "&access_token=" + accessToken + "&lang=zh_CN";
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		// 请勿轻易改变此提交方式，大部分的情况 下，提交方式都是表单提交
		// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(null,
				headers);
		// 执行HTTP请求
		ResponseEntity<String> response = client.exchange(URL, HttpMethod.POST, requestEntity, String.class);
		Map<String, Object> map = JSONObject.parseObject(response.getBody(), Map.class);
		if (map.containsKey("errcode")) {
			return "errcode";
		}
		return response.getBody();
	}
}
