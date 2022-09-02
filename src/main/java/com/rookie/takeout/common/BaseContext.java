package com.rookie.takeout.common;

/**
 * @title: BaseContext
 * @Author: Mrdong
 * @Date: 2022/8/28 12:06
 * @Description:基于ThreadLocal封装工具类，用于保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
