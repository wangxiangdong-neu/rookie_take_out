package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rookie.takeout.common.BaseContext;
import com.rookie.takeout.common.R;
import com.rookie.takeout.entity.ShoppingCart;
import com.rookie.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title: ShoppingCartController
 * @Author: Mrdong
 * @Date: 2022/9/1 11:56
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {

        ShoppingCart shoppingCart1 = shoppingCartService.addShoppingCart(shoppingCart);

        return R.success(shoppingCart1);
    }

    /**
     * 查询购物车里的数据
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingCartList() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> cleanShoppingCart() {

        shoppingCartService.cleanShoppingCart();

        return R.success("清空购物车成功");
    }

    /**
     * 减少购物车中某个菜品/套餐的数量
     *
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        //查询该用户购物车中对应的数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);

        if (shoppingCart1.getNumber() == 1) {
            //若购物车中的数量为1，则直接删除
            shoppingCartService.remove(queryWrapper);
        } else {
            //若购物车中的数量不为1，则数量减一
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartService.updateById(shoppingCart1);
        }
        return R.success("操作成功");
    }


}
