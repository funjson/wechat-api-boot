package cn.funjson.wechatapiboot.api.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;

/**
 * 获取临时待参数二维码请求dto
 * @Author fengjiansong
 * @Date 2021/2/22 2:49 下午
 */
@Data
@Builder
public class QrDto {

    /**
     * 秒单位 过期时间 max=2592000
     */
    @Max(value = 2592000)
    @Builder.Default
    private Integer expire_seconds=3600;

    /**
     * 二维码类型
     * QR_SCENE为临时的整型参数值
     * QR_STR_SCENE为临时的字符串参数值
     * QR_LIMIT_SCENE为永久的整型参数值
     * QR_LIMIT_STR_SCENE为永久的字符串参数值
     */
    @Builder.Default
    private String action_name="QR_SCENE";

    /**
     * 二维码详细信息
     */
    @Builder.Default
    private Scene action_info= Scene.builder().build();

    @Data
    @Builder
    public static
    class Scene {
        /**
         * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
         */
        @Builder.Default
        private Integer scene_id=1;

        /**
         * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        private String scene_str;
    }

}
