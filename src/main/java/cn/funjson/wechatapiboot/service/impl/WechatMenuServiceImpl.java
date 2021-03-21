package cn.funjson.wechatapiboot.service.impl;

import cn.funjson.wechatapiboot.api.dto.button.MenuDto;
import cn.funjson.wechatapiboot.common.utils.HttpUtils;
import cn.funjson.wechatapiboot.pojo.resp.WechatMenuResp;
import cn.funjson.wechatapiboot.service.WechatMenuService;
import cn.funjson.wechatapiboot.service.WechatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
public class WechatMenuServiceImpl implements WechatMenuService {

    /**
     * 创建自选菜单
     */
    private static final String CREATE_URL= "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    @Resource
    private WechatMessageService wechatMessageService;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public String createMenu(MenuDto menuDto) {

        // 获取token
        String token = wechatMessageService.getAccessToken();

        if (StringUtils.isBlank(token)){
            log.info("未能获取到对应的access_token");
            return "未能获取到对应的access_token";
        }

        ResponseEntity<WechatMenuResp> respResponseEntity = restTemplate.postForEntity(CREATE_URL+token,menuDto, WechatMenuResp .class);

        if (HttpUtils.responseCheck(respResponseEntity)){
            log.info("创建自选菜单成功");
            return respResponseEntity.getBody().getErrmsg();
        }

        return "未能创建自选菜单";
    }
}
