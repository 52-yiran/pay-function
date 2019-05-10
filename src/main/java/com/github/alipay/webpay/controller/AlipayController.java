package com.github.alipay.webpay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.github.alipay.webpay.AliPayApi;
import com.github.alipay.webpay.config.AlipayConfig;
import com.github.alipay.webpay.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("alipay")
public class AlipayController {

    @Autowired
    AlipayConfig alipayConfig;
    @Autowired
    AliPayApi aliPayApi;
    /**
     * alipayClient只需要初始化一次，后续调用不同的API都可以使用同一个alipayClient对象
     */
//    AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",APP_ID,APP_PRIVATE_KEY,"json",CHARSET,ALIPAY_PUBLIC_KEY);

//    public AlipayConfig getAlipayConfig() {
//        return alipayConfig.build();
//    }

    /**
     * 手机web支付
     */
    @RequestMapping(value = "/wapPay")
    @ResponseBody
    public void wapPay(@RequestParam("totalAmount") String totalAmount, HttpServletResponse response) {
//        getAlipayConfig();
        String body = "测试数据";
        String subject = "支付测试";
//        String totalAmount = "1";
        String passbackParams = "1";
        String returnUrl = alipayConfig.getReturn_url();
        String notifyUrl = alipayConfig.getNotify_url();

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(subject);
        //金额单位为元
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passbackParams);
        String outTradeNo = StringUtils.getOutTradeNo();
        System.out.println("wap outTradeNo>" + outTradeNo);
        model.setOutTradeNo(outTradeNo);
        model.setProductCode("QUICK_WAP_PAY");

        try {
            aliPayApi.wapPay(response, model, returnUrl, notifyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/return_url")
    public String return_url(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verify_result = AlipaySignature.rsaCheckV1(map, alipayConfig.getALIPAY_PUBLIC_KEY(), "UTF-8",
                    "RSA2");
            // 验证成功
            if (verify_result) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("return_url 验证成功");
                return "success.html";
            } else {
                System.out.println("return_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }


    /**
     * 结算
     */
    @RequestMapping(value = "/tradeOrderSettle")
    @ResponseBody
    public String tradeOrderSettle(@RequestParam("trade_no") String tradeNo) {
        try {
            AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
            model.setOutRequestNo(StringUtils.getOutTradeNo());
            model.setTradeNo(tradeNo);
            return aliPayApi.tradeOrderSettleToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 结算接口--测试
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/trade")
    @ResponseBody
    public void testSettle(@RequestParam("trade_no") String tradeNo) {
        alipayConfig.build();
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizContent("{" +
                "\"out_request_no\":\"20180727001\"," +
                "\"trade_no\":"+tradeNo+"," +
                "      \"royalty_parameters\":[{" +
                "        \"trans_out\":\"2088231777364444\"," +
                "\"trans_in\":\"2088802756841573\"," +
                "\"amount\":0.03," +
                "\"desc\":\"分账给2088101126708402\"" +
                "        }]," +
                "\"operator_id\":\"A0001\"" +
                "  }");
        AlipayTradeOrderSettleResponse response = null;
        try {
            response = alipayConfig.getAlipayClient().execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("分账调用成功！！！");
        } else {
            System.out.println("分账调用失败！！！");
        }
    }



    @RequestMapping(value = "/notify_url")
    @ResponseBody
    public String  notify_url(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println("异步通知："+entry.getKey() + " = " + entry.getValue());
            }

            boolean verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getALIPAY_PUBLIC_KEY(), "UTF-8",
                    "RSA2");
            // 验证成功
            if (verify_result) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
//                String tradeNo = params.get("out_trade_no");
//                String re = tradeOrderSettle(tradeNo);
//                System.out.println(re);
                System.out.println("notify_url 验证成功succcess");
                return "success";
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }
}
