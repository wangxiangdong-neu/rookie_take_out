package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.common.BaseContext;
import com.rookie.takeout.entity.ShoppingCart;
import com.rookie.takeout.mapper.ShoppingCartMapper;
import com.rookie.takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author LENOVO
 * @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
 * @createDate 2022-09-01 11:54:43
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

    /**
     * 清空购物车
     *
     * @return
     */
    @Override
    public void cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        this.remove(queryWrapper);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {

        //设置用户id，指定当前是哪个用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        //查询购物车中是否存在已有的菜品或套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = this.getOne(queryWrapper);

        if (shoppingCart1 != null) {
            //如果已经存在，就在原来数量基础上加一
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            this.updateById(shoppingCart1);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            if (shoppingCart.getNumber() == null) {
                shoppingCart.setNumber(1);
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }
        return shoppingCart1;
    }
}




