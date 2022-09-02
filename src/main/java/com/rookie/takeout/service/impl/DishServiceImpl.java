package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.common.CustomException;
import com.rookie.takeout.dto.DishDto;
import com.rookie.takeout.entity.Dish;
import com.rookie.takeout.entity.DishFlavor;
import com.rookie.takeout.entity.Setmeal;
import com.rookie.takeout.entity.SetmealDish;
import com.rookie.takeout.mapper.DishMapper;
import com.rookie.takeout.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: DishServiceImpl
 * @Author: Mrdong
 * @Date: 2022/8/28 19:02
 * @Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给每条口味记录设置菜品id
        flavors.forEach((dishFlavor -> dishFlavor.setDishId(dishId)));

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * 根据id获取菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavorById(Long id) {
        DishDto dishDto = new DishDto();
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息，同时更新对应的口味信息
     *
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateDishWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //获取菜品id
        Long id = dishDto.getId();

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给每条口味记录设置菜品id
        flavors.forEach((dishFlavor -> dishFlavor.setDishId(id)));

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * 批量删除菜品信息和对应的口味信息以及含有该菜品的套餐及对应的套餐菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteDishWithFlavorById(List<Long> ids) {
        //select count(*) from dish where id in (1,2,3) and status = 1
        //查询菜品状态，确定是否可以删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        //若有售卖中的菜品或售卖中的套餐中包含该菜品则无法删除
        long count = this.count(queryWrapper);

        //根据菜品id查询套餐id
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId, ids);
        //获取套餐菜品关系信息
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishLambdaQueryWrapper);
        //根据每条关系信息的套餐id查询所有关联的套餐
        for (SetmealDish setmealDish : setmealDishList) {
            LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealLambdaQueryWrapper.eq(Setmeal::getId, setmealDish.getSetmealId())
                    .eq(Setmeal::getStatus, 1);
            count += setmealService.count(setmealLambdaQueryWrapper);

        }

        if (count > 0) {
            //如果不能删除，抛出一个业务异常
            throw new CustomException("有菜品或套餐正在售卖中，无法删除！");
        }

        //批量删除菜品信息
        this.removeBatchByIds(ids);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        //删除菜品对应的口味信息
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        if (!setmealDishList.isEmpty()) {
            //删除菜品对应的套餐及套餐菜品信息
            List<Long> setmealIds = setmealDishList.stream()
                    .map(SetmealDish::getSetmealId)
                    .collect(Collectors.toList());

            setmealService.deleteSetmealWithDish(setmealIds);
        }

    }
}
