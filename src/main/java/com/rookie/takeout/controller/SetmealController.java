package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.takeout.common.R;
import com.rookie.takeout.dto.SetmealDto;
import com.rookie.takeout.entity.Category;
import com.rookie.takeout.entity.Dish;
import com.rookie.takeout.entity.Setmeal;
import com.rookie.takeout.entity.SetmealDish;
import com.rookie.takeout.service.CategoryService;
import com.rookie.takeout.service.SetmealDishService;
import com.rookie.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: SetmealController
 * @Author: Mrdong
 * @Date: 2022/8/28 19:08
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {

        setmealService.addSetmealWithDish(setmealDto);

        return R.success("保存套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> getPage(int page, int pageSize, String name) {

        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);
        //对象拷贝，把除套餐信息以外的所有都拷进setmealDtoPage里
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝，把所有套餐信息拷贝进setmealDto里
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                //获取分类名称
                String categoryName = category.getName();
                //给SetmealDto中的categoryName属性赋值
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());
        //给setmealDtoPage赋上新的值，其中带有套餐分类信息
        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 批量起售/停售套餐
     *
     * @param num
     * @param ids
     * @return
     */
    @PostMapping("/status/{num}")
    public R<String> updateSetmealStatus(@PathVariable("num") Integer num, @RequestParam List<Long> ids) {

        List<Setmeal> setmeals = new ArrayList<>();

        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(num);//修改套餐起售/停售状态，0 停售 1 起售
            setmeals.add(setmeal);
        }

        setmealService.updateBatchById(setmeals);
        return R.success("套餐修改成功");
    }

    /**
     * 根据id获取套餐及对应的菜品信息
     *
     * @return
     */
    @GetMapping(value = {"/{id}"})
    public R<Setmeal> getSetmealById(@PathVariable("id") Long id) {

        SetmealDto setmealDto = setmealService.getSetmealWithDishById(id);

        return R.success(setmealDto);
    }
    /**
     * 根据id获取套餐对应的菜品信息
     *
     * @return
     */
    @GetMapping(value = {"/dish/{id}"})
    public R<List<SetmealDish>> getSetmealDishListById(@PathVariable("id") Long id) {

        SetmealDto setmealDto = setmealService.getSetmealWithDishById(id);

        return R.success(setmealDto.getSetmealDishes());
    }

    /**
     * 修改套餐
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto) {

        setmealService.updateSetmealWithDish(setmealDto);

        return R.success("修改套餐成功");

    }


    /**
     * 根据id批量删除套餐以及对应的菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids) {

        setmealService.deleteSetmealWithDish(ids);

        return R.success("删除成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> getList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //获取该分类下的套餐列表信息
        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return R.success(setmealList);
    }


}
