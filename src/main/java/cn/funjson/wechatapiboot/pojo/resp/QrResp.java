package cn.funjson.wechatapiboot.pojo.resp;

import lombok.Data;

/**
 * @Author fengjiansong
 * @Date 2021/2/22 3:40 下午
 */
@Data
public class QrResp {

    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    private String ticket;

    /**
     * 过期时间 秒
     */
    private Integer expire_seconds;

    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;

}
