package com.ylms.messageStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ylms.common.AccessToken;
import net.sf.json.JSONObject;

public class KfService {
	private static final Logger LOG = LoggerFactory.getLogger(KfService.class);
	private static final String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

	/**
	 * 
	 * 使用客服消息接口回复多条信息给用户
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static int customSend(String content, String openId) {
		JSONObject json = new JSONObject();
		JSONObject text = new JSONObject();
		json.element("msgtype", "text");
		json.element("touser", openId);
		text.element("content", content);
		json.element("text", text);
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
		return (Integer) map.get("errcode");
	}

}
