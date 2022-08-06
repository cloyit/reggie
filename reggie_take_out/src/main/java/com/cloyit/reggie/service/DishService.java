package com.cloyit.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloyit.reggie.dto.DishDto;
import com.cloyit.reggie.entity.Dish;


public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteById(Long id);

    void updateStopStatusById(Long ids);

    void updateStartStatusById(Long ids);
}
