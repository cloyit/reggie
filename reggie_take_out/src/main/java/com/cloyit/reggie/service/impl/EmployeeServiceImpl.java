package com.cloyit.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloyit.reggie.entity.Employee;
import com.cloyit.reggie.mapper.EmployeeMapper;
import com.cloyit.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
