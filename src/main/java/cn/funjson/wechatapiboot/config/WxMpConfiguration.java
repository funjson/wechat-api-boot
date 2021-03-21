package cn.funjson.wechatapiboot.config;

import cn.funjson.wechatapiboot.component.handler.SubscribeHandler;
import me.chanjar.weixin.common.api.WxMessageInMemoryDuplicateChecker;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * @author fengjiansong
 */
@Configuration
public class WxMpConfiguration {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.appSecret}")
    private String appSecret;

    @Value("${wechat.aesKey}")
    private String aesKey;

    @Value("${wechat.token}")
    private String token;

    @Resource
    private SubscribeHandler subscribeHandler;

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    /**
     * 这个地方的配置是保存在本地，生产环境需要自己扩展，可以保存在Redis中等等
     *
     * @return WxMpConfigStorage
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl storage = new WxMpDefaultConfigImpl();
        storage.setAppId(appid);
        storage.setSecret(appSecret);
        storage.setAesKey(aesKey);
        storage.setToken(token);
        return storage;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 消息去重
        router.setMessageDuplicateChecker(new WxMessageInMemoryDuplicateChecker());

        // 关注事件
        router.rule().async(false).msgType(EVENT).event(SUBSCRIBE)
                .handler(subscribeHandler)
                .end();

        return router;
    }
}