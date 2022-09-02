package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.entity.OrderDetail;
import com.rookie.takeout.service.OrderDetailService;
import com.rookie.takeout.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-01 16:51:51
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




