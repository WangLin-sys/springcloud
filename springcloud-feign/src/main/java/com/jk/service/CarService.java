package com.jk.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("cloud-provider")
public interface CarService extends CarServer{

}
