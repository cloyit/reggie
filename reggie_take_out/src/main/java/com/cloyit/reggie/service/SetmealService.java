package com.cloyit.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloyit.reggie.dto.SetmealDto;
import com.cloyit.reggie.entity.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    void updateStopById(Long id);

    void updateStartById(Long id);

    SetmealDto getByIdWithDish(Long ids);

    void updateWithDish(SetmealDto setmealDto);

}
