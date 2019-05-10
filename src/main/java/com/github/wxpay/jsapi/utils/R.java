package com.github.wxpay.jsapi.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.W
 * @Description: TODO(返回数据)
 * @date 2018-6-23 15:07
 */
public class R extends HashMap<String, Object> {

    private final static int CODE_SUCCESS=0;

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok() {
        return ok("操作成功");
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("code", CODE_SUCCESS);
        r.put("msg", msg);
        return r;
    }

    public R put(Map<String, Object> map) {
        super.putAll(map);
        return this;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}
