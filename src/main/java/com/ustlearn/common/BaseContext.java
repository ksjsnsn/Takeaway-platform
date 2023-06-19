package com.ustlearn.common;

/**
 * 基于ThreadLocal封装工具类,用户保存和获取当前登录用户的id
 * 作用范围为一个线程内,不用担心开的多个线程会混淆
 */
public class BaseContext {
    //使用long类型是因为id为long类型
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
