package com.xkyss.boot.sample.weixin.controller;

import com.xkyss.weixin.tp.property.TpProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final TpProperties config;

    public HomeController(TpProperties config) {
        this.config = config;
    }

    @GetMapping("/hello")
    public String hello() {
        return config.getMainApp().getSecret();
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    /**
     * 例: http://localhost:8080/home/header?key=host
     *
     * @param key
     * @param request
     * @return
     */
    @GetMapping("/header")
    public String header(@RequestParam("key") String key, HttpServletRequest request) {
        return "header";
//        return ServletRequestUtil.getHeader(request, key);
    }
}
