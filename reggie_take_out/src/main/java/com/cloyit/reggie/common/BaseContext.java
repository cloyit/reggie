package com.cloyit.reggie.common;

/**
 * 通过这个类可以使项目在全局中使用ThreadLocal类，获取同一线程中，存在的属性
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值,与当前线程绑定，由threadlocal自动处理
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
