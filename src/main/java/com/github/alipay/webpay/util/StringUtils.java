package com.github.alipay.webpay.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final String _INT = "0123456789";
    private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String _ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();

    public StringUtils() {
    }

    public static String encode(String str) {
        String encode = null;

        try {
            encode = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return encode;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        key = key + System.currentTimeMillis();
        key = key.substring(0, 15);
        return key;
    }

    public static String format(String s, Map<String, String> map) {
        StringBuilder sb = new StringBuilder((int)((double)s.length() * 1.5D));

        int cursor;
        int start;
        int end;
        for(cursor = 0; (start = s.indexOf("${", cursor)) != -1 && (end = s.indexOf(125, start)) != -1; cursor = end + 1) {
            sb.append(s.substring(cursor, start));
            String key = s.substring(start + 2, end);
            sb.append((String)map.get(trim(key)));
        }

        sb.append(s.substring(cursor, s.length()));
        return sb.toString();
    }

    public static String format(String s, Object... args) {
        return MessageFormat.format(s, args);
    }

    public static String replace(String str, String regex, String... args) {
        int length = args.length;

        for(int i = 0; i < length; ++i) {
            str = str.replaceFirst(regex, args[i]);
        }

        return str;
    }

    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　\t`·•�\u0001\\f\\t\\v]", "");
    }

    public static String random(int count, StringUtils.RandomType randomType) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else {
            char[] buffer = new char[count];

            for(int i = 0; i < count; ++i) {
                if (randomType.equals(StringUtils.RandomType.INT)) {
                    buffer[i] = "0123456789".charAt(RANDOM.nextInt("0123456789".length()));
                } else if (randomType.equals(StringUtils.RandomType.STRING)) {
                    buffer[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(RANDOM.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
                } else {
                    buffer[i] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(RANDOM.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
                }
            }

            return new String(buffer);
        }
    }

    public static void main(String[] args) {
        System.out.println(random(32, StringUtils.RandomType.ALL));
    }

    public static enum RandomType {
        INT,
        STRING,
        ALL;

        private RandomType() {
        }
    }
}
