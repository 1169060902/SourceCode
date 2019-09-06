package com.acfun.DesignPatterns.Proxy;

class RealSubject implements Subject {
    public void Request() {
        System.out.println("访问真实主题方法...");
    }
}