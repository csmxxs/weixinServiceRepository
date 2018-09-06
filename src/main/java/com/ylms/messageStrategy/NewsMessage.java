package com.ylms.messageStrategy;

import java.util.List;
import java.util.Map;

import com.ylms.utils.WeixinMessageParseXmlUtil;

/**
 * @ClassName: NewsMessage
 * @Description: 图文消息模板
 * @Author: 49524
 * @Date: 2018/8/22 15:30
 * @Version 1.0
 */

public class NewsMessage extends MessageTmeplate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 图文消息个数，限制为8条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
	private List<Article> Articles;

	public NewsMessage(Map<String, String> map) {
		super(map);
	}
	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}


	@Override
	public String getMsg(Object obj) throws Exception{
		return WeixinMessageParseXmlUtil.getMessageToXml(obj);
	}

}
