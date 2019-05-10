package com.github.wxpay.qrcode.vo;

import java.io.Serializable;

/**
 * @Auther: JiaweiWu
 * @Date: 2019/4/17
 * @Description:
 */
public class WxpayVo implements Serializable {

    private String app_id;//公众账号ID
    private String mch_id;//微信支付商户号
    private String key;//API密钥
    private String app_secret;//AppSecret是APPID对应的接口密码

    private String out_trade_no;// 商户订单号
    private String currTime;//创建时间
    private String strTime;
    private String strRandom;
    private String nonce_str;//随机字符串
    private String spbill_create_ip;
    private String notify_url;
    private String trade_type;
    private String total_fee;//价格，单位-分

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getCurrTime() {
        return currTime;
    }

    public void setCurrTime(String currTime) {
        this.currTime = currTime;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getStrRandom() {
        return strRandom;
    }

    public void setStrRandom(String strRandom) {
        this.strRandom = strRandom;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }
}
