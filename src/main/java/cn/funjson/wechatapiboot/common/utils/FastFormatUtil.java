package cn.funjson.wechatapiboot.common.utils;

import cn.funjson.wechatapiboot.api.dto.WechatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

import static cn.funjson.wechatapiboot.common.utils.CharsetUtils.decodeText;

/**
 * @Author fengjiansong
 * @Date 2021/2/23 11:44 上午
 */
@Slf4j
public class FastFormatUtil {

    /**
     * 微信模板通知最大参数为1个first+3个keywords
     */
    private static final int MAX_PARAMS = 4;

    /**
     * 对微信推送公众号消息请求体封装
     * 方便直接用参数生成业务实体类
     *
     * @param openId     要推送消息的用户openid
     * @param templateId 推送消息的模板id
     * @param url        消息跳转链接
     * @param appId      跳转小程序appId
     * @param pagePath   跳转小程序页面路径
     * @param data       模板数据 结构为[value,color] ... [value,color]
     * @return WechatMessageDto
     */
    public static WechatMessageDto getMessageDto(String openId, String templateId, String url, String appId, String pagePath, @NotNull String[]... data) {

        // check data size
        int size = data.length;

        if ( size > MAX_PARAMS || size==0){
            log.info("模板data数据部分数量不正确 最大数量4 最小数量0");
            return null;
        }

        // common
        List<String> parmas = new LinkedList<>();

        parmas.add(openId);
        parmas.add(templateId);
        parmas.add(url);
        parmas.add(appId);
        parmas.add(pagePath);

        // data
        for (int i = 0; i < size; i++) {

            if (data[i].length == 1) {
                parmas.add(codeFormat(data[i][0]));
                parmas.add(null);
            } else if (data[i].length == 2) {
                parmas.add(codeFormat(data[i][0]));
                parmas.add(data[i][1]);
            } else {
                log.info("模板data数据部分结构不正确;正确结构应为:[value,color] ... [value,color];请核对传入请求体");
                parmas.add(null);
                parmas.add(null);
            }
        }

        // fill
        if (size != MAX_PARAMS){
            for(int i=0;i<MAX_PARAMS-size;i++){
                parmas.add(null);
                parmas.add(null);
            }
        }

        String [] args = parmas.toArray(new String[parmas.size()]);

        return innerCreate(args);
    }

    private static WechatMessageDto innerCreate(String[] args) {
        WechatMessageDto wechatMessageDto = WechatMessageDto.builder()
                .touser(args[0])
                .template_id(args[1])
                .url(args[2])
                .miniprogram(WechatMessageDto.MiniProgram.builder()
                        .appid(args[3])
                        .pagepath(args[4])
                        .build())
                .data(WechatMessageDto.DaTa.builder()
                        .first(WechatMessageDto.DataBase.builder()
                                .value(args[5])
                                .color(args[6])
                                .build())
                        .keyword1(WechatMessageDto.DataBase.builder()
                                .value(args[7])
                                .color(args[8])
                                .build())
                        .keyword2(WechatMessageDto.DataBase.builder()
                                .value(args[9])
                                .color(args[10])
                                .build())
                        .keyword3(WechatMessageDto.DataBase.builder()
                                .value(args[11])
                                .color(args[12])
                                .build())
                        .build())
                .build();
        return wechatMessageDto;
    }

    /**
     * 在进行配置文件读取的时候有可能会产生中文乱码问题
     * 此方法会判断字符串编码进行解码重新编码
     * @param text
     * @return
     */
    private static String codeFormat(String text){

        if(StringUtils.isBlank(text)){
            return null;
        }

        String returnStr = null;

        try {
            returnStr=decodeText(text);
        }catch (Exception e){
            log.info("获取字符串编码异常",e);
        }

        return returnStr;
    }

}
