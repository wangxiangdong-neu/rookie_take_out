package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.common.BaseContext;
import com.rookie.takeout.entity.ShoppingCart;
import com.rookie.takeout.service.ShoppingCartService;
import com.rookie.takeout.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author LENOVO
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-09-01 11:54:43
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

    @Override
    public void cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        this.remove(queryWrapper);
    }
}




