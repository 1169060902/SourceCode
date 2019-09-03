package com.acfun.DesignPatterns.Prototype;

/**
 * 原型模式的克隆分为浅克隆和深克隆，Java 中的 Object 类提供了浅克隆的 clone() 方法，
 * 具体原型类只要实现 Cloneable 接口就可实现对象的浅克隆，这里的 Cloneable 接口就是抽象原型类。其代码如下：
 *
 * 当创建的对象实例较为复杂的时候，使用原型模式可以简化对象的创建过程！
 * 扩展性好，由于写原型模式的时候使用了抽象原型类，在客户端进行编程的时候可以将具体的原型类通过配置进行读取。
 * 可以使用深度克隆来保存对象的状态，使用原型模式进行复制。当你需要恢复到某一时刻就直接跳到。比如我们的idea种就有历史版本，或则SVN中也有这样的操作。非常好用
 *
 *需要为每一个类配备一个克隆方法，而且该克隆方法位于一个类的里面，当对已有的类经行改造时需要修改源代码，违背了开闭原则。
 * 在实现深克隆的时需要编写较为复杂的代码，而且当对象之间存在多重嵌套引用的时候，为了实现深克隆，每一层对象对应的类都必须支持深克隆，实现相对麻烦。
 *
 */
public class Realizetype {
    Realizetype() {
    }

    public Object clone() throws CloneNotSupportedException {
        return (Realizetype) super.clone();
    }

}
