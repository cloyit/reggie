package com.cloyit.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloyit.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    @Update("update setmeal set status = 0 where id = #{id}")
    void updateStopById(Long id);
    @Update("update setmeal set status = 1 where id = #{id}")
    void updateStartById(Long id);
}
