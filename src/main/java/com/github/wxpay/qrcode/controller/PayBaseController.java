package com.github.wxpay.qrcode.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Auther: JiaweiWu
 * @Date: 2019/4/17
 * @Description:
 */
@Component
@PropertySource(value = {"classpath:/wxconfig.properties"},encoding="utf-8")
public abstract class PayBaseController {

    // 支付宝支付参数配置 //
//    @Value("${ALIPAY.APPID}")
//    protected String app_id;//应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
//    @Value("${ALIPAY.PRIVATEKEY}")
//    protected String merchant_private_key;//商户私钥，您的PKCS8格式RSA2私钥
//    @Value("${ALIPAY.PUBLICKEY}")
//    protected String alipay_public_key;//支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
//    @Value("${ALIPAY.NOTIFY_URL}")
//    protected String notify_url;//服务器异步通知页面路径
//    @Value("${ALIPAY.RETURNA_URL}")
//    protected String return_url;//页面跳转同步通知页面路径
//    @Value("${ALIPAY.SIGN}")
//    protected String sign_type = "RSA2";//签名方式
//    protected String charset = "utf-8";//字符编码格式
//    @Value("${ALIPAY.GATEWAY_URL}")
//    protected String gateway_url;//支付宝网关

    // 微信支付参数配置 //
    @Value("${WXPAY.APPID}")
    protected String APPID;//公众账号ID
    @Value("${WXPAY.MCHID}")
    protected String MCHID;//微信支付商户号
    @Value("${WXPAY.KEY}")
    protected String KEY;//API密钥
    @Value("${WXPAY.APPSECRET}")
    protected String APPSECRET;//AppSecret是APPID对应的接口密码
    @Value("${WXPAY.NOTIFY_URL}")
    protected String NOTIFY_URL;//回调地址。测试回调必须保证外网能访问到此地址
    @Value("${WXPAY.CREATE_IP}")
    protected String CREATE_IP;//发起请求的电脑IP

}
