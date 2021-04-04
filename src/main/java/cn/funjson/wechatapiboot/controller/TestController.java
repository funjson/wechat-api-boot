package cn.funjson.wechatapiboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fengjiansong
 * @Date 2021/3/23 10:30 上午
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping("/home")
    public String sayHello(){
        log.info("hello");
        return "hello 11";
    }
}

