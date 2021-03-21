package cn.funjson.wechatapiboot.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author fengjiansong
 * @Date 2021/2/23 10:28 上午
 */
@Data
@Builder
public class WechatMessageDto {

    /**
     * 推送用户的openId
     */
    private String touser;

    /**
     * 推送模板的id
     */
    private String template_id;

    /**
     * 模板跳转链接
     */
    private String url;

    /**
     * 小程序参数
     */
    private MiniProgram miniprogram;

    /**
     * 发送内容
     */
    private DaTa data;


    @Data
    @Builder
    public static
    class MiniProgram{

        /**
         * 要跳转的小程序appId
         */
        private String appid;

        /**
         * 要跳转的页面路径
         */
        private String pagepath;
    }

    @Data
    @Builder
    public static
    class DaTa{

        /**
         * first标签内容
         */
        private DataBase first;

        /**
         * keyword1标签内容
         */
        private DataBase keyword1;

        /**
         * keyword2标签内容
         */
        private DataBase keyword2;

        /**
         * keyword3标签内容
         */
        private DataBase keyword3;
    }

    @Data
    @Builder
    public static
    class DataBase{

        /**
         * 内容
         */
        String value;

        /**
         * 内容字体颜色
         */
        String color;
    }

}
