package com.github.alipay.webpay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:alipayconfig.properties")
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
	// 商户appid
	public  String APPID;
	//
	public  String RSA_PRIVATE_KEY;
	//
	public  String ALIPAY_PUBLIC_KEY ;
	//
	public  String notify_url;
	//
	public  String return_url;
	//
	public  String URL;

	public  String domain;
	//支付重定向页面
	public String pay_html;
	//
	public static String CHARSET = "UTF-8";
	//
	public static String FORMAT = "json";

	public static  String log_path = "/log";
	// RSA2
	public static  String SIGNTYPE = "RSA2";

	public  AlipayClient alipayClient;

//	public AlipayConfig() {
//	}

	public AlipayConfig build() {
		this.alipayClient = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGNTYPE);
		System.out.println(this.getURL());
		return this;
	}
//	public  static  AlipayConfig NEW(){
//		return new AlipayConfig();
//	}

	public AlipayClient getAlipayClient() {
		if (this.alipayClient == null) {
			System.out.println("alipayClient null");
			throw new IllegalStateException("alipayClient null");
		} else {
			System.out.println(alipayClient);
			return this.alipayClient;
		}
	}

	public String getAPPID() {
		return APPID;
	}

	public void setAPPID(String APPID) {
		this.APPID = APPID;
	}

	public String getRSA_PRIVATE_KEY() {
		return RSA_PRIVATE_KEY;
	}

	public void setRSA_PRIVATE_KEY(String RSA_PRIVATE_KEY) {
		this.RSA_PRIVATE_KEY = RSA_PRIVATE_KEY;
	}

	public String getALIPAY_PUBLIC_KEY() {
		return ALIPAY_PUBLIC_KEY;
	}

	public void setALIPAY_PUBLIC_KEY(String ALIPAY_PUBLIC_KEY) {
		this.ALIPAY_PUBLIC_KEY = ALIPAY_PUBLIC_KEY;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getDomain() {
		return domain;
	}

	public String getPay_html() {
		return pay_html;
	}

	public void setPay_html(String pay_html) {
		this.pay_html = pay_html;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setAlipayClient(AlipayClient alipayClient) {
		this.alipayClient = alipayClient;
	}
}
