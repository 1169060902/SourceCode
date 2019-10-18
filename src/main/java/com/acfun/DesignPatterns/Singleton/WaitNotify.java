package com.acfun.DesignPatterns.Singleton;

import java.util.concurrent.TimeUnit;

public class WaitNotify {

    final static Object lock = new Object();

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程 A 等待拿锁");
                synchronized (lock) {
                    try {
                        System.out.println("线程 A 拿到锁了");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("线程 A 开始等待并放弃锁");
                        lock.wait();
                        System.out.println("被通知可以继续执行 则 继续运行至结束");
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "线程 A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程 B 等待锁");
                synchronized (lock) {
                    System.out.println("线程 B 拿到锁了");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                    }
                    lock.notify();
                    System.out.println("线程 B 随机通知 Lock 对象的某个线程");
                }
            }
        }, "线程 B").start();
    }


}