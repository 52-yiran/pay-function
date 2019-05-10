package com.github.wxpay.jsapi.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.jsapi.common.WxPayApi;
import com.github.wxpay.jsapi.config.WxPayConfig;
import com.github.wxpay.jsapi.dto.WebChatDto;
import com.github.wxpay.jsapi.service.WxPayService;
import com.github.wxpay.jsapi.utils.AjaxResult;
import com.github.wxpay.jsapi.utils.HttpRequest;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("wxpay")
public class WxPayController extends WxPayConfig {
    private AjaxResult result = new AjaxResult();
    @Autowired
    WxPayService wxPayServcie;

//    @RequestMapping("/test")
//    public String test (){
//        System.out.println(appId);
//        return "";
//    }
    /**
     * 获取code的url
     * */
    @RequestMapping("/toOauth")
    public void getCodeUrl (HttpServletRequest request,HttpServletResponse response){
        String url = wxPayServcie.getCodeUrl();
        try {
//            String totalFee = request.getParameter("totalFee");
//            request.getSession().setAttribute("totalFee", totalFee);
            //清除之前的session数据
            request.getSession().removeAttribute("openId");
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取code中的回调地址
     * @param request
     * @param response
     * @param code
     * @param state
     * @return
     */
    @RequestMapping(value = "/oauth",method = RequestMethod.GET)
    public ModelAndView oauth(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code, @RequestParam("state") String state){
        try {
            System.out.println("state:"+state+" code:"+code);
            //获取openId接口
            WebChatDto webChatDto = wxPayServcie.getAccessToken(code,2);
            String openId = webChatDto.getOpenId();
            request.getSession().setAttribute("openId", openId);
            System.out.println("首次授权获取的openid:"+openId);
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/towxpay");
            return  modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * @Description 微信浏览器内微信支付/公众号支付(JSAPI)
     * @param request
     * @param
     * @return AjaxResult
     */
    @RequestMapping(value="orders", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public AjaxResult orders (@RequestParam("total_fee") String total_fee, HttpServletRequest request) {
        //订单总金额，单位为分，页面金额单位为元
//        double price = Double.parseDouble(total_fee)  * 100;
//        total_fee = String.valueOf(price).trim();
        String openId = (String) request.getSession().getAttribute("openId");
        System.out.println("支付接口，从session获取的openid:"+openId);
        if (!StringUtils.isNotEmpty(openId)) {
            result.addError("openId is null");
            return result;
        }
        try {
            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            //获取请求ip地址
            String ip = WXPayUtil.getIp(request);

            paraMap.put("appid", appId);
            paraMap.put("body", "订单支付");
            paraMap.put("mch_id", mchId);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("openid", openId);
            paraMap.put("out_trade_no", WXPayUtil.generateNonceStr());//订单号
            paraMap.put("spbill_create_ip", ip);
            paraMap.put("total_fee",total_fee);
            paraMap.put("trade_type", String.valueOf(WxPayApi.TradeType.JSAPI));
            paraMap.put("notify_url", notifyUrl);// 此路径是微信服务器调用支付结果通知路径
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式

            // 统一下单
            String unifiedorder_url = WxPayApi.unifiedorder_url;

            String xmlStr = HttpRequest.sendPost(unifiedorder_url, xml);//发送post请求"统一下单接口"返回预支付id:prepay_id

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
//                String return_code = map.get("return_code");
//                String return_msg = map.get("return_msg");
                prepay_id = (String) map.get("prepay_id");
            }
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appId);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
            String jsonStr = JSON.toJSONString(payMap);
            result.success(jsonStr);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 微信支付成功回调
     * 处理自己的业务
     */
    @RequestMapping("callback")
    public String callBack(HttpServletRequest request,HttpServletResponse response){
        //System.out.println("微信支付成功,微信发送的callback信息,请注意修改订单信息");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        InputStream is = null;
        try {
            is = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
            String xml = WXPayUtil.inputStream2String(is, "UTF-8");
            System.out.println("----接收到的数据如下：---" + xml);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map

            if("SUCCESS".equals(notifyMap.get("return_code"))){
                if("SUCCESS".equals(notifyMap.get("result_code"))){
                    String ordersSn = notifyMap.get("out_trade_no");//商户订单号
                    String amountpaid = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
                    BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);//将分转换成元-实际支付金额:元
                    //String openid = notifyMap.get("openid");  //如果有需要可以获取
                    //String trade_type = notifyMap.get("trade_type");

	                /*以下是自己的业务处理------仅做参考
	                 * 更新order对应字段/已支付金额/状态码
	                 */
//                    Orders order = ordersService.selectOrdersBySn(ordersSn);
//                    if(order != null) {
//                        order.setLastmodifieddate(new Date());
//                        order.setVersion(order.getVersion().add(BigDecimal.ONE));
//                        order.setAmountpaid(amountPay);//已支付金额
//                        order.setStatus(2L);//修改订单状态为待发货
//                        int num = ordersService.updateOrders(order);//更新order
//
//                        String amount = amountPay.setScale(0, BigDecimal.ROUND_FLOOR).toString();//实际支付金额向下取整-123.23--123
//	                	/*
//	                	 * 更新用户经验值
//	                	 */
//                        Member member = accountService.findObjectById(order.getMemberId());
//                        accountService.updateMemberByGrowth(amount, member);
//
//	                	/*
//	                	 * 添加用户积分数及添加积分记录表记录
//	                	 */
//                        pointService.updateMemberPointAndLog(amount, member, "购买商品,订单号为:"+ordersSn);
//
//                    }

                }
            }

            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
