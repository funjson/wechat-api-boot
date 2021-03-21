package cn.funjson.wechatapiboot.component.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author huan.fu 2020/12/19 - 上午10:25
 */
@Component
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        log.info("接收到一个扫码订阅事件:[{}]", wxMessage.toString());
        // 1、获取微信用户信息
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(wxMessage.getFromUser());
        if (null == wxMpUser) {
            log.warn("从微信公众号获取用户(FromUser)信息:[{}]失败.", wxMessage.getFromUser());
            return null;
        }
        log.info("根据 openId:[{}]获取到的微信用户信息:[{}]", wxMessage.getFromUser(), wxMpUser.toString());

        return null;
    }
}
