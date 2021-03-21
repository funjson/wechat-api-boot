package cn.funjson.wechatapiboot.controller;

import cn.funjson.wechatapiboot.api.dto.QrDto;
import cn.funjson.wechatapiboot.service.WechatMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.util.crypto.WxMpCryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

import static cn.funjson.wechatapiboot.common.utils.HttpUtils.getPostData;
import static cn.funjson.wechatapiboot.common.utils.HttpUtils.getUrl;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;

/**
 * 接受微信公众号回调
 *
 * @Author fengjiansong
 * @Date 2021/2/19 5:32 下午
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
public class WechatCallBackController {

    private static final String EVENT_FLAG = "<Event><![CDATA[LOCATION]]></Event>";

    private static final String EXCLUDE = "<Latitude></Latitude><Longitude></Longitude><Precision></Precision>";

    private static final String SIGN_TYPE = "aes";

    private static final String ERROR_TIPS = "服务器繁忙,请稍后重试";

    @Resource
    private WxMpService wxMpService;

    @Resource
    private WxMpMessageRouter wxMpMessageRouter;

    @Resource
    private WechatMessageService wechatMessageService;

    /**
     * 配置
     */
    @Resource
    private WxMpConfigStorage wxMpConfigStorage;

    /**
     * 对微信消息验证开关 默认false
     */
    @Value("${wechat.verify}")
    private boolean verify_flag;

    @Value("${wechat.sendMessage.first}")
    private String arg1;

    @Value("${wechat.sendMessage.keyword1}")
    private String arg2;

    @Value("${wechat.sendMessage.keyword2}")
    private String arg3;

    @Value("${wechat.sendMessage.keyword3}")
    private String arg4;

    /**
     * 微信事件回调接口
     *
     * @param requestBody
     * @param signature
     * @param timestamp
     * @param nonce
     * @param openid
     * @param encType
     * @param msgSignature
     * @return
     */
    @PostMapping(value = "/portal")
    public String entryCallback(@RequestBody String requestBody,
                                @RequestParam(name = "signature", required = false) String signature,
                                @RequestParam(name = "timestamp", required = false) String timestamp,
                                @RequestParam(name = "nonce", required = false) String nonce,
                                @RequestParam(name = "openid", required = false) String openid,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        // 明文传输下没有这些字段信息 调试阶段先关掉验证
        if (verify_flag) {
            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
            }
        }

        // 做字符串判断 因为没有location事件 但是微信回传多余的字段 影响之后解析
        if (!requestBody.contains(EVENT_FLAG)) {
            requestBody = requestBody.replace(EXCLUDE, "");
        }

        String out = "";
        WxMpXmlMessage inMessage = null;

        if (encType == null) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(requestBody);

        } else if (SIGN_TYPE.equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpCryptUtil cryptUtil = new WxMpCryptUtil(wxMpConfigStorage);
            String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, requestBody);
            log.debug("解密后的原始xml消息内容：{}", plainText);

            // 对解密后字符串做处理
            plainText = plainText.replace(EXCLUDE, "");
            inMessage = WxMpXmlMessage.fromXml(plainText);

            log.info("\n消息解密后内容为：\n[{}] ", inMessage.toString());
        }
        log.info("\n组装回复信息：[{}]", out);

        // 推送消息
        String type = inMessage.getEvent();
        log.info("判断是否推送消息 type:{} ",type);
        if (inMessage !=null && StringUtils.isNotBlank(type) && SUBSCRIBE.equals(type)){

            String [] arg1_ = new String[1];
            arg1_[0]=arg1;
            String [] arg2_ = new String[1];
            arg2_[0]=arg2;
            String [] arg3_ = new String[1];
            arg3_[0]=arg3;
            String [] arg4_ = new String[1];
            arg4_[0]=arg4;

            wechatMessageService.notifyUser(inMessage.getFromUser(),arg1_,arg2_,arg3_,arg4_);

        }else{
            log.info("inMessage is null or type is not subscribe");
        }

        return out;
    }


    /**
     * 生成待参数二维码接口
     *
     * @return
     */
    @PostMapping("/qr")
    public String getQrCode() {

        QrDto qrDto = QrDto.builder().build();

        // 获取access_token
        String qrTokenResp = wechatMessageService.getAccessToken();

        if (qrTokenResp == null){
            log.info("未能获取到access_token");
            return null;
        }

        // 获取待参数二维码 url
        String qrUrl = wechatMessageService.getQr(qrDto,qrTokenResp);

        if (qrUrl == null){
            log.info("未能获取到qr");
            return null;
        }

        return qrUrl;

    }


    /**
     * 用来和微信做第一次交互校验信息
     * 当按照要求解密后返回echoStr后 正式注册url
     *
     * @param msg_signature
     * @param timestamp
     * @param echostr
     * @param nonce
     * @return
     */
    @GetMapping("/portal")
    public String registerMessage(@RequestParam(name = "signature", required = false) String signature,
                                  @RequestParam(name = "msg_signature", required = false) String msg_signature,
                                  @RequestParam(name = "timestamp", required = false) String timestamp,
                                  @RequestParam("nonce") String nonce,
                                  @RequestParam(name = "echostr", required = false) String echostr, HttpServletRequest request) {

        String decryptStr = "";

        getPostData(request);

        getUrl(request);

        try {
                // 明文模式 || 密文模式

                String sReqMsgSig = URLDecoder.decode(msg_signature==null?signature:msg_signature, "utf-8");
                String sReqTimeStamp = URLDecoder.decode(timestamp, "utf-8");
                String sReqNonce = URLDecoder.decode(nonce, "utf-8");
                String echostr_decode = URLDecoder.decode(echostr, "utf-8");

                log.info("sReqMsgSig:{},sReqTimeStamp:{},sReqNonce:{},echostr_decode:{}", sReqMsgSig, sReqTimeStamp, sReqNonce, echostr_decode);

                decryptStr = echostr_decode;

        } catch (Exception e) {
            log.info("url decode 异常", e);
        }

        return decryptStr;
    }
}
