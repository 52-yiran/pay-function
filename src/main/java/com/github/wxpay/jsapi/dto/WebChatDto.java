package com.github.wxpay.jsapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebChatDto {

    private String accessToken;
    private long expiresIn;
    private String refreshToken;
    private String openId;
    private String scope;


}
