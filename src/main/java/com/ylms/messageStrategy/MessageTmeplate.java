package com.ylms.messageStrategy;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

import com.ylms.messageStrategy.IMsg;

public class MessageTmeplate implements IMsg, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Vector<String> v = new Vector<String>();
	//接收方
	public String ToUserName;
	//发送方
	public String FromUserName;
	public Long CreateTime;
	public String MsgType;
    public MessageTmeplate() {
    	
    }
	public MessageTmeplate(Map<String, String> wxdata) {
		String key = "";
		// 转换XML
		for (String s : wxdata.keySet()) {
			key += wxdata.get(s) + "_";
		}
		if (!v.contains(key)) {
			v.add(key);
		} else {
			return;
		}
		// 注意这两个值得调换，才能正确发送！
		FromUserName = wxdata.get("ToUserName");
		ToUserName = wxdata.get("FromUserName");
		String time = "" + System.currentTimeMillis();
		CreateTime = Long.valueOf(time.substring(0, 10));
		MsgType = wxdata.get("MsgType");
	}

	@Override
	public String getMsg(Object obj) throws Exception {
		return "";
	}

}
