package com.github.wxpay.jsapi.service;

import com.github.wxpay.jsapi.dto.WebChatDto;

public interface WxPayService {

    /**
     * 返回一个获取code的URL
     *
     * */
    String getCodeUrl();
    /**
     * 获取openID
     */
    WebChatDto getAccessToken(String code, Integer type);
    /**
     * 公众号支付，统一下单
     */
    String wxPay();
}
