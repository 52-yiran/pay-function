package com.github.wxpay.jsapi.utils;

import java.io.Serializable;

public class AjaxResult implements Serializable {
    private static final long serialVersionUID = 6439646269084700779L;
    private int code = 0;
    private String message;
    private Object data;

    public AjaxResult() {
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean hasError() {
        return this.code != 0;
    }

    public AjaxResult addError(String message) {
        this.message = message;
        this.code = 1;
        return this;
    }

    public AjaxResult addConfirmError(String message) {
        this.message = message;
        this.code = 2;
        return this;
    }

    public AjaxResult success(Object data) {
        this.data = data;
        return this;
    }
}
