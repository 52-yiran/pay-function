package com.github;

import com.github.alipay.webpay.config.AlipayConfig;
import com.github.wxpay.jsapi.service.WxPayService;
import com.github.wxpay.jsapi.value.ShopInfoTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class IndexController {

   @Autowired
   ShopInfoTest shopInfoTest;
    @Autowired
    WxPayService wxPayServcie;
    @Autowired
    AlipayConfig alipayConfig;
    /**
     * 微信、支付宝支付 二合一
     * 解决方案是通过User-Agent 来判断扫描的来源
     * User Agent中文名为用户代理，是Http协议中的一部分，属于头域的组成部分，User Agent也简称UA。它是一个特殊字符串头，是一种向访问网站提供你所使用的浏览器类型及版本、操作系统及版本、浏览器内核、等信息的标识。
     * Mozilla/5.0 (Linux; Android 4.4.4; HM NOTE 1LTEW Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/33.0.0.0 Mobile Safari/537.36 MicroMessenger/6. 0.0.54_r849063.501 NetType/WIFI
     * @param request
     * @return
     */
        @RequestMapping("/index")
        public void  index(HttpServletRequest request, HttpServletResponse response,Long shopId, Integer number ){
            String userAgent = request.getHeader("User-Agent");
            log.info(userAgent);
            int payWay = 0;
            //todo 商品数据写入session中:商品名称，商品数量、价格、支付金额
            if (shopId != null) {
                //支付金额
                double totalFee = 0;
                if (shopInfoTest.getShopId1().equals(shopId)) {
                    try {
                        totalFee = number * Double.parseDouble(shopInfoTest.getPrice1());
                    } catch (Exception e) {
                       System.out.println("------------"+e);
                    }
                    request.getSession().setAttribute("totalFee", totalFee);
                    //存入cookie
                    Cookie cookie = new Cookie("totalFee", String.valueOf(totalFee));
                    response.addCookie(cookie);
                } else if (shopInfoTest.getShopId2().equals(shopId)) {
                    try{
                        totalFee = number * Double.parseDouble(shopInfoTest.getPrice2());
                    }catch (Exception e){

                    }
                    request.getSession().setAttribute("totalFee", totalFee);
                    //存入cookie
                    Cookie cookie = new Cookie("totalFee", String.valueOf(totalFee));
                    response.addCookie(cookie);
                }
            }
            String agent = userAgent.toLowerCase();
            if (agent.indexOf("micromessenger")>0) {
                //用户使用微信访问页面
                payWay = 1;
                //支付类型
                Cookie cookie = new Cookie("payWay",String.valueOf(payWay));
                response.addCookie(cookie);
                System.out.println("微信...");
                //清除之前的session中得openId数据
                request.getSession().removeAttribute("openId");
                String url = wxPayServcie.getCodeUrl();
                try {
                    response.sendRedirect(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(agent.indexOf("alipayclient")>0){
                //用户使用支付宝访问页面
                payWay = 2;
                //支付类型
                Cookie cookie = new Cookie("payWay",String.valueOf(payWay));
                response.addCookie(cookie);
                System.out.println("支付宝...");
                try {
                    response.sendRedirect(alipayConfig.getPay_html());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    /**
     * 支付页面
     * @return
     */
    @RequestMapping("/towxpay")
    public String towxpay() {
        return "wxpay.html";
    }
}
