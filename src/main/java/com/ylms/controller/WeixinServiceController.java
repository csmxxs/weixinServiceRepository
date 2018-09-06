package com.ylms.controller;

import com.alibaba.fastjson.JSONObject;
import com.ylms.common.UserMessage;
import com.ylms.messageStrategy.Article;
import com.ylms.messageStrategy.KfService;
import com.ylms.messageStrategy.NewsMessage;
import com.ylms.messageStrategy.Qrcode;
import com.ylms.messageStrategy.QrcodeCreate;
import com.ylms.messageStrategy.TextMessage;
import com.ylms.service.UserService;
import com.ylms.utils.CheckUtils;
import com.ylms.utils.StringUtil;
import com.ylms.utils.WeixinMessageParseXmlUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: WeixinTokenController
 * @Description: TODO
 * @Author: 49524
 * @Date: 2018/8/22 9:30
 * @Version 1.0
 */
@Controller
//优先读取该配置文件设置的值
@PropertySource(value = { "classpath:application.yml" }, encoding = "UTF-8")
public class WeixinServiceController {
	private static final Logger LOG = LoggerFactory.getLogger(WeixinServiceController.class);
	// 处理请求的线程池
	private static final ThreadPoolExecutor threadpoolexecutor = new ThreadPoolExecutor(25, 50, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());
	@Autowired
	private UserService userService;
	@Autowired
	private WeixinMessageParseXmlUtil weixinMessageParseXmlUtil;
	private static final Lock LOCK = new ReentrantLock();
	@Value("${wx.messagesTemplateCode}")
	private String messagesTemplateCode;// 扫码关注回复模板
	@Value("${wx.messagesTemplateSecond}")
	private String messagesTemplateSecond;// 扫码关注回复模板
	@Value("${wx.messagesTemplateSearch}")
	private String messagesTemplateSearch;// 搜索关注回复模板
    @Autowired
    private Qrcode qrcode;
	/**
	 * @Author: Xxs
	 * @Description: 校验微信服务器
	 * @Date: 18:35 2018/8/20
	 * @Param: [req, resp]
	 * @return: void
	 * @throws IOException
	 **/
	@RequestMapping("authenticationService")
	public void weixinService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		// 首次请求时验证
		if (echostr != null && !echostr.equals("")) {
			if (CheckUtils.checkSignature(signature, timestamp, nonce)) {
				// out.close(),response对象处理完信息后会自动关闭流
				resp.getWriter().write(echostr);
				return;
			} else {
				resp.getWriter().write("");
				return;
			}
		}
		// 异步回复微信服务器,防止微信重发信息
		threadpoolexecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					resp.getWriter().write("");
				} catch (IOException e) {
					LOG.error("发送错误 msg={}", "原因", e);
					e.printStackTrace();
				}
			}
		});

		LOCK.lock();
		try {
			// 设置返回数据的编码格式
			resp.setCharacterEncoding(weixinMessageParseXmlUtil.CHARSET_TYPE);
			Map<String, String> wxdata = weixinMessageParseXmlUtil.parseXml(req);
			// REQ_MESSAGE_TYPE_TEXT接收文本消息
			if (weixinMessageParseXmlUtil.REQ_MESSAGE_TYPE_TEXT.equals(wxdata.get("MsgType"))) {
				LOG.info("2.1解析消息内容为:接收文本消息");
				this.textMessage(wxdata, resp);
				return;
			}

			// REQ_MESSAGE_TYPE_EVENT事件推送
			if (weixinMessageParseXmlUtil.REQ_MESSAGE_TYPE_EVENT.equals(wxdata.get("MsgType"))) {
				// Event事件类型

				// 关注公众号事件
				if (weixinMessageParseXmlUtil.EVENT_TYPE_SUBSCRIBE.equals(wxdata.get("Event"))) {
					this.subscribe(wxdata, resp);
					return;
				}
				// 已关注公众号扫描带参数二维码进入公众号事件
				if (weixinMessageParseXmlUtil.EVENT_TYPE_SCAN.equals(wxdata.get("Event"))) {
					this.welcomyjk(wxdata, resp);
					return;
				}
				// 取消关注事件
				if (weixinMessageParseXmlUtil.EVENT_TYPE_UNSUBSCRIBE.equals(wxdata.get("Event"))) {
					this.unsubscribe(wxdata, resp);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("发送错误 msg={}", "原因", e);
		} finally {
			LOG.info("程序正在释放锁!处理下一个请求。。。。。");
			LOCK.unlock();
		}

	}

	/**
	 * 扫码带参数二维码,且用户已关注公众号事件回复
	 * 
	 * @param wxdata
	 * @param resp
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void welcomyjk(Map<String, String> wxdata, HttpServletResponse response) throws Exception {
		// 获取用户基本信息
		String user = UserMessage.getUserMessage(wxdata.get("FromUserName"));
		Map<String, String> map = JSONObject.parseObject(user, Map.class);
		wxdata.put("MsgType", "news");
		NewsMessage scan = new NewsMessage(wxdata);
		List<Article> list = new ArrayList<Article>();
		Article article = new Article();
		article.setTitle((String) map.get("nickname") + "," + this.messagesTemplateSearch);
		article.setDescription("大家好!");
		article.setPicUrl("PicUrl");
		article.setUrl("setUrl");
		list.add(article);
		scan.setArticles(list);
		scan.setArticleCount(1);
		String msg = scan.getMsg(scan);
		LOG.info("转换成xml格式后的回复消息是:\n" + msg);
		response.getWriter().write(msg);
	}

	/**
	 * 用户取消关注的操作
	 */
	private void unsubscribe(Map<String, String> wxdata, HttpServletResponse resp) {
		LOG.info("用户取消关注完成!按微信官方的规定，正在删除用户信息!");
		userService.deleteByOpenId(wxdata.get("FromUserName"));
	}

	/**
	 * @Author: Xxs
	 * @Description: 用户第一次关注公众号的回复
	 * @Date: 11:27 2018/8/23
	 * @Param: [wxdata, expireKey, out, isreturn]
	 * @return: void
	 **/
	@SuppressWarnings("unchecked")
	private void subscribe(Map<String, String> wxdata, HttpServletResponse response) throws Exception {
		// 获取用户基本信息
		String user = UserMessage.getUserMessage(wxdata.get("FromUserName"));
		Map<String, String> map = JSONObject.parseObject(user, Map.class);
		// 用户关注的场景
		String subscribe_scene = (String) map.get("subscribe_scene");
		if (user != null && !user.equals("errcode")) {

			if (subscribe_scene.equals("ADD_SCENE_SEARCH")) {
				// 搜素关注的回复操作
				wxdata.put("MsgType", "text");
				TextMessage search = new TextMessage(wxdata,"您好!@" + (String) map.get("nickname") + "," + StringUtil.ascii2native(this.messagesTemplateSearch));
				response.getWriter().write(search.getMsg(search));
			}
			if (subscribe_scene.equals("ADD_SCENE_QR_CODE")) {
				// 扫码关注的回复操作
				String text = "@" + (String) map.get("nickname") + ","
						+ StringUtil.ascii2native(this.messagesTemplateCode) + "";
				Integer i = KfService.customSend(text, wxdata.get("FromUserName"));
				if (i == 0) {
					LOG.info("正在回复第二条文本信息");
					Integer integer=KfService.customSend(StringUtil.ascii2native(this.messagesTemplateSecond),wxdata.get("FromUserName"));
					if(integer==0) {
						//回复图片
						LOG.info("正在回复第三条图片信息");
						String ticket=QrcodeCreate.getqrCodeImages(qrcode);
						QrcodeCreate.getImages(ticket,wxdata.get("FromUserName"));
					}
				}
			}
		} else {
			response.getWriter().write("");
		}
	}

	/**
	 * 接收文本信息，并回復
	 * 
	 * @param wxdata
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void textMessage(Map<String, String> wxdata, HttpServletResponse response) throws Exception {

		// 获取用户基本信息
		String user = UserMessage.getUserMessage(wxdata.get("FromUserName"));
		Map<String, String> map = JSONObject.parseObject(user, Map.class);
//		String m = this.messagesTemplateCode;
//		TextMessage text = new TextMessage(wxdata,
//				"@" + (String) map.get("nickname") + "," + StringUtil.ascii2native(m));
//		LOG.info("转换成xml格式后的回复消息是:\n" + text.getMsg(text));
//		response.getWriter().write(text.getMsg(text));
//		String second=this.messagesTemplateSecond;
//		TextMessage text = new TextMessage(wxdata,StringUtil.ascii2native(second));
//		LOG.info("转换成xml格式后的回复消息是:\n" + text.getMsg(text));
//		response.getWriter().write(text.getMsg(text));

	}
}
