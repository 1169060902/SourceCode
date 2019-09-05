package com.acfun.DesignPatterns.Bulider;

/**
 * (5) 客户类。
 */
public class Client {
    public static void main(String[] args) {
        Builder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        Product product = director.construct();
        product.show();
    }
}