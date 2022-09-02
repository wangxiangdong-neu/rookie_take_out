package com.rookie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.takeout.dto.SetmealDto;
import com.rookie.takeout.entity.Dish;
import com.rookie.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增套餐，同时需要保存套餐和菜品的关联关系
    public void addSetmealWithDish(SetmealDto setmealDto);

    //根据id获取套餐及对应的菜品信息
    public SetmealDto getSetmealWithDishById(Long id);

    //修改套餐以及对应的菜品信息
    public void updateSetmealWithDish(SetmealDto setmealDto);

    //根据id批量删除套餐以及对应的菜品
    public void deleteSetmealWithDish(List<Long> ids);

    //根据套餐id获取菜品列表数据
    public List<Dish> getSetmealDishListById(Long id);
}
