package cn.funjson.wechatapiboot.pojo.resp;

import lombok.Data;

/**
 * @Author fengjiansong
 * @Date 2021/2/23 2:35 下午
 */
@Data
public class WechatMessageResp {

    private String errcode;

    private String errmsg;

    private String msgid;
}
