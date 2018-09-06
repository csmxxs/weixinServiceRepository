package com.ylms.entity;

/**
 * @ClassName: User
 * @Description: TODO
 * @Author: 49524
 * @Date: 2018/8/20 16:44
 * @Version 1.0
 */

public class User {
    //
    private Long id;
    //用户openId
    private String openId;
    //扫描次数
    private Integer scanNumber;
    //赠送状态
    private int presenterState;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScanNumber() {
        return scanNumber;
    }

    public void setScanNumber(Integer scanNumber) {
        this.scanNumber = scanNumber;
    }

    public int getPresenterState() {
        return presenterState;
    }

    public void setPresenterState(int presenterState) {
        this.presenterState = presenterState;
    }
}
