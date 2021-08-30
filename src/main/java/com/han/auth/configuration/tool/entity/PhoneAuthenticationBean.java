package com.han.auth.configuration.tool.entity;

import java.io.Serializable;

public class PhoneAuthenticationBean implements Serializable {

    private long id;
    private int code;
    private String exID;
    private String content;
    private String phone;

    @Override
    public String toString() {
        return "PhoneAuthenticationBean{" +
                "id=" + id +
                ", code=" + code +
                ", exID='" + exID + '\'' +
                ", content='" + content + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getExID() {
        return exID;
    }

    public void setExID(String exID) {
        this.exID = exID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
