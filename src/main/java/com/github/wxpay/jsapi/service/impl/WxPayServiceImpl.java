package com.github.wxpay.jsapi.service.impl;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.dialect.Props;
import com.github.wxpay.jsapi.common.WxPayApi;
import com.github.wxpay.jsapi.config.WxPayConfig;
import com.github.wxpay.jsapi.dto.WebChatDto;
import com.github.wxpay.jsapi.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    Props props = new Props("wxconfig.properties");
    @Override
    public String getCodeUrl() {
        String codeUrl =String.format(WxPayApi.code_url,props.getProperty("WXPAY.APPID"),props.getProperty("WXPAY.GETCODE.CALLBACK"),String.valueOf(WxPayApi.SCOPE.snsapi_base),"123") ;
        return codeUrl;
    }

    @Override
    public WebChatDto getAccessToken(String code, Integer type) {
        // 请求获取access_token
        // 开放平台
        String url = String.format(WxPayApi.AccessToken_url,props.getProperty("WXPAY.APPID"),props.getProperty("WXPAY.OPEN.APPSECRET"),code);
        if(type == 2){
            // 公众平台
            url = String.format(WxPayApi.AccessToken_url,props.getProperty("WXPAY.APPID"), props.getProperty("WXPAY.APPSECRET"),code);
        }
        // 获取请求接口返回的信息
        JSONObject jsonObject = null;
        try {
            String s = HttpUtil.get(url);
            jsonObject = new JSONObject(s);
        } catch (Exception e) {
            log.info("获取TOKEN,{}",e.getMessage());
        }
        // 微信用户唯一标识id
        String openId = String.valueOf(jsonObject.get("openid"));
        // 许可token
        String accessToken = String.valueOf(jsonObject.get("access_token"));
        WebChatDto webChat = new WebChatDto();
        if(StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(accessToken)){
            webChat.setAccessToken(accessToken);
            webChat.setOpenId(openId);
        }else{
            throw new ValidateException("微信授权登录失败");
        }
        return webChat;
    }

    /**
     * 统一下单
     * @return
     */
    @Override
    public String wxPay() {

        return null;
    }

    /**
     * 统一下单参数设置
     */
    private String WXParamGenerate(String description, String outTradeNo, Long totalFee){
        WxPayConfig wxPayConfig = new WxPayConfig();
        return null;
    }

}
