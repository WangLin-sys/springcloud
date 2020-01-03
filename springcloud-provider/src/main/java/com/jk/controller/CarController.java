package com.jk.controller;

import com.jk.service.CarServer;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController implements CarServer {


    @Override
    public void queryCar(String carName) {

        System.out.println("成功接收到查询请求"+carName);
    }


}
