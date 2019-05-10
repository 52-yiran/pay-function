package com.github.wxpay.jsapi.value;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:testvalue.properties")
@ConfigurationProperties(prefix = "shop")
public class ShopInfoTest {

//    @Value("${shop.shopId1}")
//    protected Long shopId1;
//    @Value("${shop.shopId2}")
//    private Long shopId2;
//    @Value("${shop.price1}")
//    private String price1;
//    @Value("${shop.price2}")
//    private String price2;

    protected Long shopId1;
    private Long shopId2;
    private String price1;
    private String price2;
}
