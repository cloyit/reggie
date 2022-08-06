package com.cloyit.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cloyit.reggie.dto.DishDto;
import com.cloyit.reggie.entity.Dish;
import com.cloyit.reggie.entity.DishFlavor;
import com.cloyit.reggie.mapper.DishMapper;
import com.cloyit.reggie.service.DishFlavorService;
import com.cloyit.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorService dishFlavorService;

    public DishServiceImpl() {
        super();
    }

    @Override
    //开启事务
    // TODO 事务？
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //菜品ID
        // 这一步由MP实现  获取dishid
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //批量保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(long id) {
        //根据ID查询菜品信息
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //根据菜品ID查询菜品口味列表
        LambdaQueryWrapper<DishFlavor> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        this.updateById(dishDto);

        //清理原有口味数据
        LambdaQueryWrapper<DishFlavor> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        List<DishFlavor> list = flavors.stream().map((i) -> {
            i.setDishId(null);
            i.setDishId(dishDto.getId());
            return i;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(list);
    }

    @Override
    public void deleteById(Long id) {
        dishMapper.deleteById(id);
    }

    @Override
    public void updateStopStatusById(Long ids) {
        dishMapper.updateStopStatusById(ids);
    }


    @Override
    public void updateStartStatusById(Long ids) {
        dishMapper.updateStartStatusById(ids);
    }

}
