package com.cloyit.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloyit.reggie.common.R;
import com.cloyit.reggie.entity.Employee;
import com.cloyit.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    //注入接口实现类
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的代码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据提交的username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到,返回登录失败
        if(emp==null){
            return R.error("用户名不存在");
        }
        //4.进行密码对比,如果不一致返回密码错误
        if(!emp.getPassword().equals(password)){
            return  R.error("密码错误");
        }
        //5.查看员工状态,如果禁用,返回禁用结果
        if(emp.getStatus()!=1){
            return  R.error("你已经离职了");
        }
        //6.登录成功,将员工id存入session,返回成功的结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出登录
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存当前员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
