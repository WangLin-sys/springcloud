package com.jk.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface CarServer {

    @GetMapping("car/queryName")
    void queryCar(@RequestParam("carName") String carName);
}
