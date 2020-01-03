package com.jk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pageController {

    /**
     * @return 跳转 show 页面
     */
    @RequestMapping("carshow")
    public String carshow(){

        return "Carshow";
    }





}
