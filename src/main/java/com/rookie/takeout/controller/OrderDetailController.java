package com.rookie.takeout.controller;

import com.rookie.takeout.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: OrderDetailController
 * @Author: Mrdong
 * @Date: 2022/9/1 16:53
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;



}
