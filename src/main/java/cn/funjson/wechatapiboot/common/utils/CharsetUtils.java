package cn.funjson.wechatapiboot.common.utils;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class CharsetUtils {

    private static final String DEFAULT="utf-8";

    public static String decodeText(String str) throws UnsupportedEncodingException {
        return new String(str.getBytes(formatCode(str)), Charset.defaultCharset());
    }

    private static String formatCode(String str){
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s;
            }
        } catch (Exception exception) {

        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {

        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {

        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {

        }
        return DEFAULT;
    }
}
