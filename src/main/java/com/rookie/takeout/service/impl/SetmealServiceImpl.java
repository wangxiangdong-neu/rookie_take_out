package com.rookie.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookie.takeout.common.CustomException;
import com.rookie.takeout.dto.SetmealDto;
import com.rookie.takeout.entity.Dish;
import com.rookie.takeout.entity.Setmeal;
import com.rookie.takeout.entity.SetmealDish;
import com.rookie.takeout.mapper.SetmealMapper;
import com.rookie.takeout.service.DishService;
import com.rookie.takeout.service.SetmealDishService;
import com.rookie.takeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: SetmealServiceImpl
 * @Author: Mrdong
 * @Date: 2022/8/28 19:07
 * @Description:
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Transactional
    @Override
    public void addSetmealWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(item -> item.setSetmealId(setmealDto.getId()));

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id获取套餐及对应的菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getSetmealWithDishById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //查询套餐基本信息，从setmeal表查询
        Setmeal setmeal = this.getById(id);

        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询当前套餐对应的菜品信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);

        //给菜品信息赋值
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;
    }

    /**
     * 修改套餐以及对应的菜品信息
     *
     * @param setmealDto
     */
    @Transactional
    @Override
    public void updateSetmealWithDish(SetmealDto setmealDto) {
        //修改套餐信息
        this.updateById(setmealDto);

        Long id = setmealDto.getId();//获取套餐id

        //删除之前的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(queryWrapper);

        //获取菜品信息并设置套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(item -> item.setSetmealId(id));

        //保存新的菜品信息
        setmealDishService.saveBatch(setmealDishes);


    }

    /**
     * 根据id批量删除套餐以及对应的菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteSetmealWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        long count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            //如果不能删除，抛出一个业务异常
            throw new CustomException("有套餐正在售卖中，删除失败！");
        }

        ////如果可以删除，先批量删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);
        //删除套餐对应的菜品信息
        setmealDishService.remove(queryWrapper);
    }

    /**
     * 根据套餐id获取菜品列表数据
     * @param id
     * @return
     */
    @Override
    public List<Dish> getSetmealDishListById(Long id) {
        //先根据套餐id查询套餐菜品数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        //根据套餐菜品表的数据获取菜品id
        List<Long> dishIds = setmealDishes.stream()
                .map(SetmealDish::getDishId)
                .collect(Collectors.toList());

        //查询菜品信息
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getId,dishIds);
        List<Dish> dishes = dishService.list(dishLambdaQueryWrapper);

        return dishes;
    }

}
