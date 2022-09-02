package com.rookie.takeout.service;

import com.rookie.takeout.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LENOVO
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-09-01 16:51:11
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 提交订单
     * @param orders
     */
    public void submitOrder(Orders orders);

}
