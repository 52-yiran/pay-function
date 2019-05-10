package com.github.wxpay.jsapi.common;

public class WxPayApi {
    /**
     * 获取code
     * String.format是个参数 appid.redirect_uri.scope.state
     */
    public static final String code_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    /**
     * 获取openid
     *  String.format三个参数appid、secret、code
     */
    public static final String AccessToken_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
     */
    public static final String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 单次分账接口
     */
    public static  final String profitsharing_url = "https://api.mch.weixin.qq.com/secapi/pay/profitsharing";

    public static enum TradeType {
        JSAPI,
        NATIVE,
        APP,
        WAP,
        MICROPAY,
        MWEB,
        PAP;

        private TradeType() {
        }
    }
    public static enum SCOPE{
        snsapi_base,
        snsapi_userinfo;
        private SCOPE(){

        }
    }
}
