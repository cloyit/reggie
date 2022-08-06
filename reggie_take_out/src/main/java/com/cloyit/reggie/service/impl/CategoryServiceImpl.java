package com.cloyit.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloyit.reggie.common.CustomException;
import com.cloyit.reggie.entity.Category;
import com.cloyit.reggie.entity.Dish;
import com.cloyit.reggie.entity.Setmeal;
import com.cloyit.reggie.mapper.CategoryMapper;
import com.cloyit.reggie.service.CategoryService;
import com.cloyit.reggie.service.DishService;
import com.cloyit.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private DishService dishService;
    @Override
    public void remove(long id) {
        LambdaQueryWrapper<Dish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(wrapper1);
        if (count1 > 0)
            throw new CustomException("当前分类已关联菜品，无法删除");

        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(wrapper2);
        if (count2 > 0)
            throw new CustomException("当前分类已关联套餐，无法删除");

        super.removeById(id);
    }
}
