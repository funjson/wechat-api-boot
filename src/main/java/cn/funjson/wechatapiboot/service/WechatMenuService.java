package cn.funjson.wechatapiboot.service;


import cn.funjson.wechatapiboot.api.dto.button.MenuDto;

public interface WechatMenuService {

    String createMenu(MenuDto menuDto);

}
