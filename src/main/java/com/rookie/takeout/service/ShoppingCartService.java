package com.rookie.takeout.service;

import com.rookie.takeout.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LENOVO
* @description 针对表【shopping_cart(购物车)】的数据库操作Service
* @createDate 2022-09-01 11:54:43
*/
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 清空购物车
     */
    void cleanShoppingCart();
}
