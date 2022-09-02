package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.takeout.common.R;
import com.rookie.takeout.dto.DishDto;
import com.rookie.takeout.entity.Category;
import com.rookie.takeout.entity.Dish;
import com.rookie.takeout.entity.DishFlavor;
import com.rookie.takeout.service.CategoryService;
import com.rookie.takeout.service.DishFlavorService;
import com.rookie.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: DishController
 * @Author: Mrdong
 * @Date: 2022/8/28 19:06
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品
     *
     * @return
     */
    @PostMapping()
    public R<String> addDish(@RequestBody DishDto dishDto) {

        dishService.saveWithFlavor(dishDto);

        return R.success("添加菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> getPage(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name)
                .orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                //获取分类名称
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }


    /**
     * 根据id获取菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable("id") Long id) {

        DishDto dishDto = dishService.getDishWithFlavorById(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {

        dishService.updateDishWithFlavor(dishDto);

        return R.success("修改菜品成功");
    }

    /**
     * 批量起售/停售菜品
     *
     * @param num
     * @param ids
     * @return
     */
    @PostMapping("/status/{num}")
    public R<String> updateDishStatus(@PathVariable("num") Integer num, @RequestParam List<Long> ids) {

        List<Dish> dishList = new ArrayList<>();
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(num);//修改菜品起售/停售状态，0 停售 1 起售
            dishList.add(dish);
        }

        dishService.updateBatchById(dishList);
        return R.success("菜品状态修改成功");
    }

    /**
     * 批量删除菜品信息和对应的口味信息以及含有该菜品的套餐及对应的套餐菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids) {

        dishService.deleteDishWithFlavorById(ids);

        return R.success("菜品删除成功");
    }

    /**
     * 根据条件查询菜品信息
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getList(Dish dish) {

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            //把dish的数据拷贝到dishDto中
            BeanUtils.copyProperties(item, dishDto);

            //根据菜品id查询菜品对应的口味信息
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(flavors);

            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
    /*@GetMapping("/list")
    public R<List<Dish>> getList(Dish dish) {

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }*/
}
