package com.ylms.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.ylms.messageStrategy.Article;
import com.ylms.messageStrategy.NewsMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: WeixinMessageParseXmlUtil
 * @Description: 消息转换工具
 * @Author: 49524
 * @Date: 2018/8/22 14:58
 * @Version 1.0
 */
@Component
public class WeixinMessageParseXmlUtil {
	private static final Logger LOG = LoggerFactory.getLogger(WeixinMessageParseXmlUtil.class);
	/**
	 * 编码类型
	 */
	public final String CHARSET_TYPE = "UTF-8";
	/**
	 * 请求消息类型：文本
	 */
	public final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：语音
	 */
	public final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：视频
	 */
	public final String REQ_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 请求消息类型：链接
	 */
	public final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：小视频
	 */
	public final String REQ_MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

	/**
	 * 请求消息类型：事件推送
	 */
	public final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 返回消息类型：文本
	 */
	public final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 消息返回类型：图片
	 */
	public final String RESP_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 消息返回类型:语音
	 */
	public final String RESP_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 消息返回类型：音乐
	 */
	public final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 消息返回类型：图文
	 */
	public final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 消息返回类型：视频
	 */
	public final String RESP_MESSAGE_TYPE_VIDEO = "video";

	/**
	 * 事件类型:订阅
	 */
	public final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：取消订阅
	 */
	public final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：SCAN(关注用户扫描带参二维码)(用户已关注时的事件)
	 */
	public final String EVENT_TYPE_SCAN = "SCAN";

	/**
	 * 事件类型：location(上报地理位置)
	 */
	public final String EVENT_TYPE_LOCATION = "location";

	/**
	 * 事件类型：CLICK(点击菜单拉取消息)
	 */
	public final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 事件类型：VIEW(自定义菜单URl视图)
	 */
	public final String EVENT_TYPE_VIEW = "VIEW";

	/**
	 * 事件类型：TEMPLATESENDJOBFINISH(模板消息送达情况提醒)
	 */
	public final String EVENT_TYPE_TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";

	/**
	 * @Author: Xxs
	 * @Description: 解析微信服务器发过来的xml格式的消息将其转换为map
	 * @Date: 15:37 2018/8/22
	 * @Param: [request]
	 * @return: java.util.Map<java.lang.String,java.lang.String>
	 **/
	public Map<String, String> parseXml(HttpServletRequest request) {

		InputStream inputStream = null;
		try {
			// 将解析结果存储在HashMap中
			Map<String, String> map = new HashMap<String, String>();
			// 从request中得到输入流
			inputStream = request.getInputStream();
			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到XML的根元素
			Element root = document.getRootElement();
			// 得到根元素的所有子节点
			List<Element> elementList = root.elements();
			// 判断又没有子元素列表
			if (elementList.size() == 0) {
				map.put(root.getName(), root.getText());
			} else {
				for (Element e : elementList)
					map.put(e.getName(), e.getText());
			}
			// 释放资源
			inputStream.close();
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("发生错误 msg={}", "原因", e);
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception s) {

				}
			}
			return null;
		}
	}

	/**
	 * @Author: Xxs
	 * @Description: 文本消息对象转换成xml
	 * @Date: 16:02 2018/8/22
	 * @Param: [textMessage]
	 * @return: java.lang.String
	 **/
	public static String getMessageToXml(Object obj) throws Exception{
		xstream.alias("xml", obj.getClass());
		if (obj instanceof NewsMessage) {
			NewsMessage newsMessage=(NewsMessage)obj;
			for(Article a:newsMessage.getArticles()) {
				xstream.alias("item",a.getClass());
			}
		}
		return xstream.toXML(obj);
	}

	/**
	 * @Author: Xxs
	 * @Description: 对象到xml的处理, 扩展xstream，使其支持CDATA块
	 * @Date: 16:04 2018/8/22
	 * @Param:
	 * @return:
	 **/
	private static XStream xstream = new XStream(new XppDriver() {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@Override
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata && !isInteger(text)) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
				/*
				 * 方法二：推荐，速度最快 判断是否为整数
				 * 
				 * @param str 传入的字符串
				 * 
				 * @return 是整数返回true,否则返回false
				 */

				protected boolean isInteger(String str) {
					Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
					return pattern.matcher(str).matches();
				}
			};
		}
	});

}
