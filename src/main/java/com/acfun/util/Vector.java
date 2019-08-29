package com.acfun.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

/**
 * {@code Vector} is synchronized.  If a thread-safe 线程安全的数组
 *
 * @param <E> 支持泛型
 *            AbstractList 实现了大部分接口
 *            List 实现
 *            RandomAccess 表明ArrayList支持快速（通常是固定时间）随机访问。此接口的主要目的是允许一般的算法更改其行为，从而在将其应用到随机或连续访问列表时能提供良好的性能。
 *            Cloneable 支持克隆
 *            Serializable 支持序列化
 *             对List 大量方法进行了同步锁操作
 */
public class Vector<E>
        extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    /**
     * 保存vector中元素的数组。vector的容量是数组的长度，数组的长度最小值为vector的元素个数。
     * <p>
     * 任何在vector最后一个元素之后的数组元素是null。
     *
     * @serial
     */
    protected Object[] elementData;

    /**
     * vector中实际的元素个数。
     *
     * @serial
     */
    protected int elementCount;

    /**
     * vector需要自动扩容时增加的容量。
     * <p>
     * 当vector的实际容量elementCount将要大于它的最大容量时，vector自动增加的容量。
     * <p>
     * 如果capacityIncrement小于或等于0，vector的容量需要增长时将会成倍增长。
     *
     * @serial
     */
    protected int capacityIncrement;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 有参数构造
     *
     * @param initialCapacity   初始容量
     * @param capacityIncrement 每次自增量
     */
    public Vector(int initialCapacity, int capacityIncrement) {
        //调用父类初始化
        super();
        if (initialCapacity < 0)
            //参数非法 必须大于等于零的初始化
            throw new IllegalArgumentException("Illegal Capacity: " +
                    initialCapacity);
        //初始化数组
        this.elementData = new Object[initialCapacity];
        //初始化自增量
        this.capacityIncrement = capacityIncrement;
    }

    public Vector() {
        //默认容量为10
        this(10);
    }

    /**
     * 有参数构造
     *
     * @param initialCapacity 初始容量
     */
    public Vector(int initialCapacity) {
        //默认自增为0
        this(initialCapacity, 0);
    }

    /**
     * 线程安全
     * 将vector中的所有元素拷贝到指定的数组anArray中
     *
     * @param anArray 指定数组
     */
    public synchronized void copyInto(Object[] anArray) {
        //数组拷贝
        System.arraycopy(elementData, 0, anArray, 0, elementCount);
    }

    /**
     * 线程安全
     * 将底层数组的容量调整为当前vector实际元素的个数，来释放空间。
     */
    public synchronized void trimToSize() {
        //数组操作修改
        modCount++;
        //历史长度
        int oldCapacity = elementData.length;
        //当前数组实际长度小于数组长度
        if (elementCount < oldCapacity) {
            //将底层数组的长度调整为实际大小
            elementData = Arrays.copyOf(elementData, elementCount);
        }
    }

    /**
     * 线程安全
     *
     * @param minCapacity 最小容量
     */
    public synchronized void ensureCapacity(int minCapacity) {
        //判断是否需要扩容
        if (minCapacity > 0) {
            modCount++;
            ensureCapacityHelper(minCapacity);
        }
    }

    /**
     * ensureCapacity()是同步的，它可以调用本方法来扩容，而不用承受同步带来的消耗
     *
     * @param minCapacity 扩容最小容量
     */
    private void ensureCapacityHelper(int minCapacity) {
        // 当前容量小于 需要扩容的容量
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 扩容，保证vector至少能存储minCapacity个元素。
     * 首次扩容时，newCapacity = oldCapacity + ((capacityIncrement > 0) ?capacityIncrement : oldCapacity);即如果capacityIncrement>0，就加capacityIncrement，如果不是就增加一倍。
     * <p>
     * 如果第一次扩容后，容量还是小于minCapacity，就直接将容量增为minCapacity。
     *
     * @param minCapacity 至少需要的容量
     */
    private void grow(int minCapacity) {
        // 获取当前数组的容量
        int oldCapacity = elementData.length;
        // 扩容。新的容量=当前容量+当前容量/2.即将当前容量增加一倍
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                capacityIncrement : oldCapacity);
        //如果扩容后的容量还是小于想要的最小容量
        if (newCapacity - minCapacity < 0)
            ///将扩容后的容量再次扩容为想要的最小容量
            newCapacity = minCapacity;
        ///如果扩容后的容量大于临界值，则进行大容量分配
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    /**
     * 进行大容量分配
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    private static final long serialVersionUID = -2767605614048989439L;

    /**
     * 线程安全
     * 设置容量设置
     *
     * @param newSize 最新长度
     */
    public synchronized void setSize(int newSize) {
        //操作修改
        modCount++;
        //最新长度大于当前容量 需要扩容
        if (newSize > elementCount) {
            ensureCapacityHelper(newSize);
        } else {
            //最新长度小于当前容量 对于超出长度的需要滞空
            for (int i = newSize; i < elementCount; i++) {
                elementData[i] = null;
            }
        }
        //设置最新长度
        elementCount = newSize;
    }

    public synchronized int capacity() {
        return elementData.length;
    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public synchronized int size() {
        return elementCount;
    }
}
