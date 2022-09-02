package com.rookie.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rookie.takeout.entity.Category;

public interface CategoryService extends IService<Category> {

    public void removeCategoryById(Long id);
}
