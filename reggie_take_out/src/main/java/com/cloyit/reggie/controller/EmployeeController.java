package com.cloyit.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloyit.reggie.common.R;
import com.cloyit.reggie.entity.Employee;
import com.cloyit.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "员工信息管理相关接口")
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

    @PostMapping
    /**
     * 新增员工
     */
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);

        return R.success("新增员工成功");

    }


    @GetMapping("/page")
    public R<Page> selectAllEmployee(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据ID修改信息
     * 通用的方法
     * @param employee
     * @param request
     * @return
     */
    @PutMapping
    public R<String> open(@RequestBody Employee employee,
                          HttpServletRequest request){

        log.info(employee.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);

        return R.success("员工信息更新成功");

    }

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return  R.error("没有查询到");

    }

}
