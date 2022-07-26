package com.cloyit.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloyit.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(long id);
}
