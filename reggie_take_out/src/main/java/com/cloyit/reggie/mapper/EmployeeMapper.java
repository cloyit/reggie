package com.cloyit.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloyit.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工数据访问接口
 * mabatis-plus
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
