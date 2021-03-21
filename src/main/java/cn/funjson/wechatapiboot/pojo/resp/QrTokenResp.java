package cn.funjson.wechatapiboot.pojo.resp;

import lombok.Data;

/**
 * @Author fengjiansong
 * @Date 2021/2/22 4:18 下午
 */
@Data
public class QrTokenResp {

    /**
     * 返回的token
     */
    private String access_token;

    /**
     * 过期时间 秒
     */
    private Integer expires_in;
}
