package cn.funjson.wechatapiboot.service;


import cn.funjson.wechatapiboot.api.dto.QrDto;

/**
 * @Author fengjiansong
 * @Date 2021/2/23 10:24 上午
 */
public interface WechatMessageService {

    /**
     * 获取access_token
     * @return
     */
    String getAccessToken();

    /**
     * 推送用户消息
     * @param openId 用户openId
     * @param args 数据 结构 [value,color] ... [value,color]
     * @return
     */
    String notifyUser(String openId,String [] ... args);

    /**
     * 获取二维码地址
     * @param qrDto
     * @param accessToken
     * @return
     */
    String getQr(QrDto qrDto, String accessToken);

}
