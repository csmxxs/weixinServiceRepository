package com.ylms.messageStrategy;

import java.util.Map;

import com.ylms.utils.WeixinMessageParseXmlUtil;

/**
 * @ClassName: MusicMessage
 * @Description: TODO
 * @Author: 49524
 * @Date: 2018/8/22 15:32
 * @Version 1.0
 */

public class MusicMessage extends MessageTmeplate{
	//音乐标题
    private String Title;
    //音乐描述
    private String Description;
    //音乐链接
    private String MusicURL;
    //高质量音乐链接，WIFI环境优先使用该链接播放音乐
    private String HQMusicUrl;
    //缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
    private String ThumbMediaId;
    
	public MusicMessage(Map<String, String> map) {
		super(map);
	}
	public String getMsg(Object obj) throws Exception{
		return WeixinMessageParseXmlUtil.getMessageToXml(obj);
	}
}
