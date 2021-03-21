package cn.funjson.wechatapiboot.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Author fengjiansong
 * @Date 2021/2/20 3:47 下午
 */
@Slf4j
public class HttpUtils {

    /**
     * 获取请求体内容
     * @param request
     * @return
     */
    public static String getPostData(HttpServletRequest request) {

        InputStream inputStream = null;

        BufferedReader in = null;

        StringBuffer sb = new StringBuffer();

        try {
            inputStream = request.getInputStream();
            String s;
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
            log.info("异常");
        } finally {
            try {
                in.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("[postData]:", sb.toString());
        return sb.toString();
    }

    /**
     * 获取完整请求url
     * @param request
     * @return
     */
    public static String getUrl(HttpServletRequest request) {
        String url = "";
        url = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getServletPath();
        if (request.getQueryString() != null) {
            url += "?" + request.getQueryString();
        }
        log.info("url:{}", url);
        return url;
    }

    public static boolean responseCheck(ResponseEntity responseEntity){
        if (HttpStatus.OK.getReasonPhrase().equals(responseEntity.getStatusCode()) || responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            return true;
        }
        return false;
    }

}
