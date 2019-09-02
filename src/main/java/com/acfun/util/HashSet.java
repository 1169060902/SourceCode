package com.acfun.util;

import java.util.*;

/**
 * TreeMap存储的是元素。
 *
 * @param <E> AbstractSet 继承了AbstractSet，实现Set接口时需要实现的工作量大大减少了。
 *            Set 实现了Set，实现了Set中声明的操作set的基本方法。
 *            Cloneable 表明其可以调用clone()方法来返回实例的field-for-field拷贝。
 *            Serializable 表明该类是可以序列化的。
 */
public class HashSet<E> extends AbstractSet<E>
        implements Set<E>, Cloneable, java.io.Serializable {

    /**
     * 序列
     */
    static final long serialVersionUID = -5024744406713321676L;
    /**
     * 存储数据 说明HashSet是依赖于HashMap的，底层就是一个HashMap实例。
     */
    private transient HashMap<E,Object> map;
    /**
     * 前面讲了，HashSet是依赖于HashMap的，底层就是一个HashMap实例。
     * HashMap是保存键值对的，但我们保存hashSet的时候肯定只是想保存key，
     * 那么调用hashMap(key,value)时value应该传什么值呢？PRESENT就是value
     */
    private static final Object PRESENT = new Object();

    public HashSet() {
        map = new HashMap<>();
    }

    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }



    @Override
    public Iterator<E> iterator() {
        //对key进行迭代
        return map.keySet().iterator();
    }


    @Override
    public int size() {
        //底层map的长度
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E e) {
        //添加元素 默认值为PRESENT
        return map.put(e, PRESENT)==null;
    }

    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    public void clear() {
        map.clear();
    }

}
