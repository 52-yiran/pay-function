package com.github.wxpay.qrcode.controller;

import com.github.wxpay.qrcode.utils.*;
import com.github.wxpay.qrcode.vo.WxpayVo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Auther: JiaweiWu
 * @Date: 2019/4/17
 * @Description: 微信支付后台接口
 */

@RestController
@RequestMapping(value = "/wxpay")
public class WxpayController extends PayBaseController {

    private static Logger logger = LoggerFactory.getLogger(WxpayController.class);

    /**
     * 微信支付->扫码支付(模式二)->统一下单->微信二维码
     * @return
     */
    @GetMapping("/qrcode")
    public void wxpayPay(HttpServletResponse response) {
        String urlCode = null;
        // 获取订单信息
        WxpayVo vo = new WxpayVo();
        String out_trade_no = UUID.randomUUID().toString().replace("-", "");
        vo.setOut_trade_no(out_trade_no);
        // 账号信息
        vo.setApp_id(APPID);
        vo.setMch_id(MCHID);
        vo.setKey(KEY);
        String currTime = PayToolUtil.getCurrTime();
        vo.setCurrTime(currTime);
        String strTime = currTime.substring(8, currTime.length());
        vo.setStrTime(strTime);
        String strRandom = String.valueOf(PayToolUtil.buildRandom(4));
        vo.setStrRandom(strRandom);
        String nonce_str = strTime + strRandom;
        vo.setNonce_str(nonce_str);
        vo.setSpbill_create_ip(CREATE_IP);
        vo.setNotify_url(NOTIFY_URL);
        vo.setTrade_type("NATIVE");
        String total_fee = "1";
        vo.setTotal_fee(total_fee);//价格的单位为分

        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        packageParams.put("appid", APPID);//公众账号ID
        packageParams.put("mch_id", MCHID);//商户号
        packageParams.put("nonce_str", nonce_str);//随机字符串
        packageParams.put("body", "资源");  //商品描述
        packageParams.put("out_trade_no", out_trade_no);//商户订单号
        packageParams.put("total_fee", total_fee); //标价金额 订单总金额，单位为分
        packageParams.put("spbill_create_ip", CREATE_IP);//终端IP APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
        packageParams.put("notify_url", NOTIFY_URL);//通知地址 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
        packageParams.put("trade_type", "NATIVE");//交易类型 NATIVE 扫码支付
        // 签名
        String sign = PayToolUtil.createSign("UTF-8", packageParams, KEY);
        packageParams.put("sign", sign);

        // 将请求参数转换为xml格式的string
        String requestXML = PayToolUtil.getRequestXml(packageParams);
        logger.info("requestXML:{}", requestXML);

        // 调用微信支付统一下单接口
        String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);
        logger.info("resXml: {}", resXml);

        // 解析微信支付结果
        Map map = null;
        try {
            map = XMLUtil4jdom.doXMLParse(resXml);
            logger.info("map: {}", map);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 返回微信支付的二维码连接
        urlCode = (String) map.get("code_url");
        logger.info("urlCode:{}", urlCode);

        try {
            int width = 300;
            int height = 300;
            //二维码的图片格式
            String format = "gif";
            Hashtable hints = new Hashtable();
            //内容所使用编码
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix;
            bitMatrix = new MultiFormatWriter().encode(urlCode, BarcodeFormat.QR_CODE, width, height, hints);
            QRUtil.writeToStream(bitMatrix, format, response.getOutputStream());
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 微信支付-回调
     * @param request
     * @param response
     */
    @PostMapping("/notify")
    public String wxpayNotify(HttpServletRequest request, HttpServletResponse response) {
        //读取参数
        InputStream inputStream ;
        StringBuffer sb = null;
        try {
            sb = new StringBuffer();
            inputStream = request.getInputStream();
            String s ;
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //解析xml成map
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = XMLUtil4jdom.doXMLParse(sb.toString());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //过滤空 设置 TreeMap
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = map.get(parameter);
            String v = "";
            if(null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }

        //判断签名是否正确
        if(PayToolUtil.isTenpaySign("UTF-8", packageParams, KEY)) {
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if("SUCCESS".equals((String)packageParams.get("result_code"))){
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String)packageParams.get("mch_id");
                String openid = (String)packageParams.get("openid");
                String is_subscribe = (String)packageParams.get("is_subscribe");
                String out_trade_no = (String)packageParams.get("out_trade_no");

                String total_fee = (String)packageParams.get("total_fee");

                //////////执行自己的业务逻辑////////////////
                //暂时使用最简单的业务逻辑来处理：只是将业务处理结果保存到session中
                //（根据自己的实际业务逻辑来调整，很多时候，我们会操作业务表，将返回成功的状态保留下来）
                request.getSession().setAttribute("_PAY_RESULT", "OK");

                logger.info("支付成功");

                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return ("fail");
            }
            //------------------------------
            //处理业务完毕
            //------------------------------
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/xml; charset=utf-8");
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else{
            logger.info("通知签名验证失败");
        }
        return ("success");
    }

}
