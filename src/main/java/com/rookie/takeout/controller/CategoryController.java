package com.rookie.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rookie.takeout.common.R;
import com.rookie.takeout.entity.Category;
import com.rookie.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: CategoryController
 * @Author: Mrdong
 * @Date: 2022/8/28 12:54
 * @Description:分类管理
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping()
    public R<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> getPage(int page, int pageSize) {
        //构造分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long ids) {

        categoryService.removeCategoryById(ids);

        return R.success("删除分类成功");
    }

    /**
     * 修改分类信息
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }


    /**
     * 根据条件查询分类信息
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getList(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }

}
