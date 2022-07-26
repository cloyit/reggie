package com.cloyit.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloyit.reggie.entity.Setmeal;
import com.cloyit.reggie.mapper.SetmealMapper;
import com.cloyit.reggie.service.SetmealService;

import org.springframework.stereotype.Service;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

}
