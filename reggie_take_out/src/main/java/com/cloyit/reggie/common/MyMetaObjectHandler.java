package com.cloyit.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component   // 加入该注解后的类会被spring扫描并初始化为bean对象，用于在应用其他位置注入
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 通过自动注入获取session中的值
     */
    @Autowired
    HttpServletRequest request;
    /**
     * 公共字段填充，当调用插入方法时，会自动调用方法，为下列属性赋值，不需要再service层单独赋值，减少代码量
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充[insert]...");
        log.info(metaObject.toString());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        /**
         * 获取值的两种方法，1.通过自动注入注入request对象，从session中获取
         * 通过request对象注入，会导致单一性只能够获取指定的用户，不能复用
         * 2.通过ThreadLocal类获取（过程为同一线程，将数据放在这个类中，通过在同一线程获取数据）
         */
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        //metaObject.setValue("createUser", request.getSession().getAttribute("user"));
        //有局限性不能复用
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    /**
     *公共字段填充，当调用更新方法时，会自动调用方法，为下列属性赋值，不需要再service层单独赋值，减少代码量
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充[update]...");
        log.info(metaObject.toString());


        metaObject.setValue("updateTime", LocalDateTime.now());
        log.info("这是在请求域中获取的{}",request.getSession().getAttribute("employee"));
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

}
