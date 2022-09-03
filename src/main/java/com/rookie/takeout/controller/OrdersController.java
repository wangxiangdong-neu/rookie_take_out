package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.takeout.common.BaseContext;
import com.rookie.takeout.common.R;
import com.rookie.takeout.dto.OrdersDto;
import com.rookie.takeout.entity.OrderDetail;
import com.rookie.takeout.entity.Orders;
import com.rookie.takeout.entity.ShoppingCart;
import com.rookie.takeout.service.OrderDetailService;
import com.rookie.takeout.service.OrdersService;
import com.rookie.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: OrdersController
 * @Author: Mrdong
 * @Date: 2022/9/1 16:52
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 提交订单
     *
     * @return
     */
    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody Orders orders) {

        ordersService.submitOrder(orders);

        return R.success("下单成功");
    }

    /**
     * 前台获取订单分页信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(value = {"/userPage"})
    public R<Page> getPage(int page, int pageSize) {
        //构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行分页查询
        ordersService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();

            //把订单基础信息拷贝进ordersDto
            BeanUtils.copyProperties(item, ordersDto);

            //根据订单id查询订单明细
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);

            //把数据封装进ordersDto
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;

        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);

    }

    /**
     * 后台获取订单分页信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(value = {"/page"})
    public R<Page> getPageInBackend(int page, int pageSize, Long number, String beginTime, String endTime) {

        //构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number != null, Orders::getNumber, number);
        queryWrapper.between((beginTime != null && endTime != null), Orders::getOrderTime, beginTime, endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行分页查询
        ordersService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();

            //把订单基础信息拷贝进ordersDto
            BeanUtils.copyProperties(item, ordersDto);

            //根据订单id查询订单明细
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);

            //把数据封装进ordersDto
            ordersDto.setOrderDetails(orderDetails);

            return ordersDto;

        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);

    }

    /**
     * 修改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateOrder(@RequestBody Orders orders) {
        ordersService.updateById(orders);
        return R.success("订单修改成功");
    }

    /**
     * 再来一单
     * @param orders
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {

        ordersService.again(orders);

        return R.success("成功");
    }

}
