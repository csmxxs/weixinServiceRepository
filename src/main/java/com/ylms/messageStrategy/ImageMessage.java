package com.ylms.messageStrategy;

/**
 * @ClassName: ImageMessage
 * @Description: 图片消息对象封装
 * @Author: 49524
 * @Date: 2018/8/22 15:31
 * @Version 1.0
 */

public class ImageMessage {

    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private String Mediald;

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMediald() {
        return Mediald;
    }

    public void setMediald(String mediald) {
        Mediald = mediald;
    }

    public ImageMessage(String toUserName, String fromUserName, String createTime, String msgType, String mediald) {
        ToUserName = toUserName;
        FromUserName = fromUserName;
        CreateTime = createTime;
        MsgType = msgType;
        Mediald = mediald;
    }
}
