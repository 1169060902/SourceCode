package com.acfun.DesignPatterns.Singleton;

/**
 * 懒汉式单例
 * 该模式的特点是类加载时没有生成单例，只有当第一次调用 getlnstance 方法时才去创建这个单例。代码如下：
 */
class LazySingleton {
    /**
     * 保证线程安全
     */
    private static volatile LazySingleton lazySingleton;

    /**
     * 构造私有化 防止外部调用
     */
    private LazySingleton() {
    }

    /**
     * 保证线程安全
     *
     * @return 返回对象
     */
    public synchronized static LazySingleton getLazySingleton() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
            return lazySingleton;
        } else {
            return lazySingleton;
        }
    }

}
