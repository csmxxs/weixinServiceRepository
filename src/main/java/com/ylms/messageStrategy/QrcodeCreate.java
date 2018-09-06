package com.ylms.messageStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ylms.common.AccessToken;

import net.sf.json.JSONObject;

/**
 * 二维码创建
 * 
 * @author Administrator
 *
 */
public class QrcodeCreate {
	private static final String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	private static final String TICKET = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	private static final Logger LOG = LoggerFactory.getLogger(QrcodeCreate.class);

	/** 获取二维码图片 */
	@SuppressWarnings("unchecked")
	public static String getqrCodeImages(Qrcode qrcode) {

		// {"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm3sUw==","expire_seconds":60,"url":"http:\/\/weixin.qq.com\/q\/kZgfwMTm72WWPkovabbI"}

		JSONObject json = new JSONObject();
		JSONObject scene = new JSONObject();
		json.element("action_name", qrcode.getAction_name());
		// 临时二维码
		if (qrcode.getAction_name().equals("QR_SCENE")) {
			json.element("expire_seconds", qrcode.getExpire_seconds());
			JSONObject scene_id = new JSONObject();
			scene_id.element("scene_id", qrcode.getScene_id());
			scene.element("scene", scene_id);
		}
		// 永久二维码
		if (qrcode.getAction_name().equals("QR_LIMIT_SCENE") || qrcode.getAction_name().equals("QR_LIMIT_STR_SCENE")) {
			JSONObject scene_str = new JSONObject();
			scene_str.element("scene_str", qrcode.getScene_str());
			scene.element("scene", scene_str);
		}
		json.element("action_info", scene);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		Map<String, Object> map = null;
		try {
			URL realUrl = new URL(url + AccessToken.accessToken.get(0));
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(json.toString());
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			LOG.info("客服消息result：" + result);
			map = (Map<String, Object>) com.alibaba.fastjson.JSONObject.parseObject(result, Map.class);
		} catch (Exception e) {
			LOG.info("向客服发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}

		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return (String) map.get("ticket");
	}

	public static void getImages(String ticket,String openId) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(null,
				headers);
		// 执行HTTP请求
		try {
			ResponseEntity<String> resp = client.exchange(TICKET + URLEncoder.encode(ticket, "UTF-8"), HttpMethod.GET,
					requestEntity, String.class);
			LOG.info(resp.getBody());
			//调用客户消息接口，返回图片给用户
			KfService.customSend(resp.getBody(), openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
