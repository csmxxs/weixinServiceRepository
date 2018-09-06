package com.ylms.messageStrategy;

import java.util.Map;

import com.ylms.utils.WeixinMessageParseXmlUtil;

/**
 * @ClassName: TextMessage
 * @Description: 文本消息模板
 * @Author: 49524
 * @Date: 2018/8/22 15:30
 * @Version 1.0
 */

public class TextMessage extends MessageTmeplate{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private String Content;
	public TextMessage(Map<String, String> map,String Content) {
		super(map);
		this.Content = Content;
	}
	public TextMessage(String Content) {
		this.Content = Content;
	}
	
	@Override
	public String getMsg(Object obj) throws Exception{
		return WeixinMessageParseXmlUtil.getMessageToXml(obj);
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
}
