package com.cloyit.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloyit.reggie.common.CustomException;
import com.cloyit.reggie.dto.SetmealDto;
import com.cloyit.reggie.entity.Setmeal;
import com.cloyit.reggie.entity.SetmealDish;
import com.cloyit.reggie.mapper.SetmealDishMapper;
import com.cloyit.reggie.mapper.SetmealMapper;
import com.cloyit.reggie.service.SetmealDishService;
import com.cloyit.reggie.service.SetmealService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealMapper setmealMapper;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert
        this.save(setmealDto);
        //保存套餐和菜品的关联信息，操作setmeal_dish，执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {

                //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);


    }

    @Override
    public void updateStopById(Long id) {

        setmealMapper.updateStopById(id);
    }

    @Override
    public void updateStartById(Long id) {
        setmealMapper.updateStartById(id);
    }

    /**
     * 在新增套餐时回显
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //查询套餐状态，确定是否可以修改
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        List<Long> list = new ArrayList<>();
        list.add(setmealDto.getId());
        queryWrapper.in(Setmeal::getId,list);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);

        if(count > 0){
            //如果不能修改，抛出业务运行异常
            throw new CustomException("套餐正在售卖中，不能修改");
        }

        //如果可以修改，则先删除表中的对应数据
        this.removeByIds(list);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,list);

        //删除记录套餐中具体餐品的表中的指定数据
        setmealDishService.remove(lambdaQueryWrapper);


        //saveWithDish(setmealDto);

        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }
}
