package com.github.alipay.webpay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.github.alipay.webpay.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class AliPayApi  {
    @Autowired
    AlipayConfig alipayConfig;

//    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

//    private static final String ALIPAY_GATEWAY_NEW = alipayConfig.getURL();
    public AliPayApi() {

    }

    public  AlipayConfig getAlipayConfig() {
        return alipayConfig.build();
    }

    public  void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        String form = wapPayStr(response, model, returnUrl, notifyUrl);
        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
        response.getWriter().write(form);
        response.getWriter().flush();
    }

    public  String wapPayStr(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        getAlipayConfig();
//        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getURL(), alipayConfig.getAPPID(), alipayConfig.getRSA_PRIVATE_KEY(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, alipayConfig.getALIPAY_PUBLIC_KEY(), AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setBizModel(model);
        return ((AlipayTradeWapPayResponse)alipayConfig.getAlipayClient().pageExecute(alipayRequest)).getBody();
    }

    public  AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model) throws AlipayApiException {
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        return (AlipayTradeOrderSettleResponse)alipayConfig.getAlipayClient().execute(request);
    }

    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator iter = requestParams.keySet().iterator();

        while(iter.hasNext()) {
            String name = (String)iter.next();
            String[] values = (String[])((String[])requestParams.get(name));
            String valueStr = "";

            for(int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        return params;
    }
}
