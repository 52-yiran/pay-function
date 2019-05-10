package com.github.wxpay.jsapi.config;

import com.github.wxpay.jsapi.common.WxPayApi;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:/wxconfig.properties"},encoding="utf-8")
@Getter
@Setter
public class WxPayConfig {

    //appid、body、mch_id、nonce_str、openid、out_trade_no
    // spbill_create_ip、total_fee、trade_type、notify_url、sign
    @Value("${WXPAY.APPID}")
    protected String appId;
    @Value("${WXPAY.MCHID}")
    protected String mchId;//商户ID
    @Value("${WXPAY.KEY}")
    protected String paternerKey;//API 密钥
    @Value("${WXPAY.NOTIFY_URL}")
    protected String notifyUrl;//回调地址

    private String body;//所支付的名称
    private String nonceStr;//随机字符串用WXPayUtil中的generateNonceStr()即可,就是生成UUID的方法；
    private String openId;
    private String outTradeNo;//自己后台生成的订单号,只要保证唯一就好
    private String spbillCreateIp;//IP地址
    private String totalFee;//支付金额 单位：分,为了测试此值给1,表示支付1分钱
    private WxPayApi.TradeType tradeType;//支付类型,公众号支付此处给“JSAPI”
    private String sign;

}
