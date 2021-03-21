package cn.funjson.wechatapiboot.common.enums;

import lombok.Getter;

@Getter
public enum ButtonEnum {

    CLICK("click", 0),
    VIEW("view", 1),
    SCANCODE_PUSH("scancode_push", 3),
    SCANCODE_WAITMSG("scancode_waitmsg", 4),
    PIC_SYSPHOTO("pic_sysphoto", 5),
    PIC_PHOTO_OR_ALBUM("pic_photo_or_album", 6),
    PIC_WEIXIN("pic_weixin", 7),
    MEDIA_ID("media_id", 8),
    VIEW_LIMIT("view_limited", 9),
    LOCATION_SELECT("location_select", 10);

    private String key;

    private int group;

    ButtonEnum(String key, int group) {
        this.key = key;
        this.group = group;
    }

}
