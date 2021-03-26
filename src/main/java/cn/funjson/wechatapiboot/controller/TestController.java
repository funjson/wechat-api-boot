package cn.funjson.wechatapiboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fengjiansong
 * @Date 2021/3/23 10:30 上午
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/home")
    public String sayHello(){
        return "hello 6";
    }
}

