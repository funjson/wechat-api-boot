package cn.funjson.wechatapiboot.service.impl;


import cn.funjson.wechatapiboot.api.dto.QrDto;
import cn.funjson.wechatapiboot.api.dto.WechatMessageDto;
import cn.funjson.wechatapiboot.common.utils.FastFormatUtil;
import cn.funjson.wechatapiboot.pojo.resp.QrResp;
import cn.funjson.wechatapiboot.pojo.resp.QrTokenResp;
import cn.funjson.wechatapiboot.pojo.resp.WechatMessageResp;
import cn.funjson.wechatapiboot.service.WechatMessageService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static cn.funjson.wechatapiboot.common.utils.HttpUtils.responseCheck;


/**
 * 公众号给用户推送消息
 * @Author fengjiansong
 * @Date 2021/2/23 10:21 上午
 */
@Service
@Slf4j
public class WechatMessageServiceImpl implements WechatMessageService {

    /**
     * 获取access_token
     */
    private static final String AC_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 推送用户消息
     */
    private static final String MESSAGE_URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 获取待参数二维码ticket地址
     */
    private static final String QR_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    /**
     * 通过ticket获取qr_url
     */
    private static final String QR_GET_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    /**
     * 模板Id
     */
    @Value("${wechat.sendMessage.templateId}")
    private String templateId;

    /**
     * 消息跳转url
     */
    @Value("${wechat.sendMessage.jumpUrl}")
    private String jumpUrl;

    /**
     * 配置
     */
    @Resource
    private WxMpConfigStorage wxMpConfigStorage;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String getAccessToken() {

        String appId = wxMpConfigStorage.getAppId();
        String secret = wxMpConfigStorage.getSecret();

        String tokenUrl = AC_TOKEN + "&appid=" + appId + "&secret=" + secret;
        log.info("获取access_token url:{}", tokenUrl);

        ResponseEntity<QrTokenResp> respResponseEntity = restTemplate.getForEntity(tokenUrl, QrTokenResp.class);

        log.info("respResponseEntity:{}", JSONObject.toJSONString(respResponseEntity));

        QrTokenResp qrTokenResp = null;

        if (responseCheck(respResponseEntity)) {
            qrTokenResp = respResponseEntity.getBody();
        }

        if (qrTokenResp == null) {
            log.info("未能获取到access_token");
            return null;
        }

        return qrTokenResp.getAccess_token();
    }

    @Override
    public String notifyUser(String openId,String [] ... args) {

        // 请求获取token
        String token=getAccessToken();

        String url=jumpUrl;
        WechatMessageDto wechatMessageDto = FastFormatUtil.getMessageDto(openId,templateId,url,null,null,args);
        log.info("消息推送功能向微信发送请求体为：{} \n url:{}",JSONObject.toJSONString(wechatMessageDto),url);

        // 请求微信给用户推送消息
        ResponseEntity<WechatMessageResp> respResponseEntity = restTemplate.postForEntity(MESSAGE_URL+token,wechatMessageDto,WechatMessageResp.class);
        if (responseCheck(respResponseEntity)){
            log.info("消息推送成功：{}", JSONObject.toJSONString(respResponseEntity.getBody()));
        }

        return respResponseEntity.getBody().getMsgid();
    }

    @Override
    public String getQr(QrDto qrDto, String accessToken) {

        String url = QR_URL + accessToken;
        log.info("请求获取待参数qr url:{}", url);

        ResponseEntity<QrResp> qrResp_respResponseEntity = restTemplate.postForEntity(url, qrDto, QrResp.class);
        QrResp qrResp;
        String ticket = null;

        if (responseCheck(qrResp_respResponseEntity)) {
            qrResp = qrResp_respResponseEntity.getBody();
            ticket = qrResp.getTicket();
        }

        log.info("response: {}", JSONObject.toJSONString(qrResp_respResponseEntity));

        if (ticket == null) {
            log.info("未能获取到ticket");
            return null;
        }

        // 根据ticket生成qr图片链接 qrUrl
        String encodeTicket;
        String qrUrl = QR_GET_URL;

        try {
            encodeTicket = URLEncoder.encode(ticket, "utf-8");
            qrUrl = QR_GET_URL + encodeTicket;
        } catch (UnsupportedEncodingException e) {
            log.info("urlEncoder encode 异常 ", e);
        }

        return qrUrl;

    }
}
