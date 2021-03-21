package cn.funjson.wechatapiboot.api.dto.button;

public abstract class MenuDto {

    /**
     * 按钮类型
     */
    private String type;

    /**
     * 按钮名称
     */
    private String name;

    /**
     * 菜单KEY值，用于消息接口推送，不超过128字节
     */
    private String key;

    /**
     * 点击按钮跳转连接
     */
    private String url;

    /**
     * 小程序的appid（仅认证公众号可配置）
     */
    private String appid;

    /**
     * 小程序的页面路径
     */
    private String pagepath;

    /**
     * 调用新增永久素材接口返回的合法media_id
     */
    private String media_id;

    public abstract void add(MenuDto menuDto);
}
