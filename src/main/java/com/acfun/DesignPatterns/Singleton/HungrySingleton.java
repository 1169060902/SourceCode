package com.acfun.DesignPatterns.Singleton;

/**
 * 饿汉式单例
 * 该模式的特点是类一旦加载就创建一个单例，保证在调用 getInstance 方法之前单例已经存在了。
 */
public class HungrySingleton {
    /**
     * 类加载时初始化对象 只加载一次
     */
    private static final HungrySingleton hungrySingleton = new HungrySingleton();

    /**
     * 构造私有化 防止外部调用
     */
    private HungrySingleton() {
    }

    /**
     * 获取单例
     *
     * @return 结果
     */
    public static HungrySingleton getHungrySingleton() {
        return hungrySingleton;
    }
}
