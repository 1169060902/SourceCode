package com.acfun.collection;

import java.io.Serializable;
import java.util.*;

/**
 * @param <E> 支持泛型
 *            AbstractList 继承了AbstractList。AbstractList提供List接口的骨干实现，以最大限度地减少“随机访问”数据存储（如ArrayList）实现Llist所需的工作
 *            List 实现了所有可选列表操作。
 *            Serializable 表明该类具有序列化功能
 *            Cloneable 表明其可以调用clone()方法来返回实例的field-for-field拷贝。
 *            RandomAccess 表明ArrayList支持快速（通常是固定时间）随机访问。此接口的主要目的是允许一般的算法更改其行为，从而在将其应用到随机或连续访问列表时能提供良好的性能。
 */
public class ArrayList<E>
        extends AbstractList<E>
        implements List<E>,
        Serializable,
        Cloneable,
        RandomAccess {
    /**
     * 初始化默认容量。
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 指定该ArrayList容量为0时，返回该空数组。
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 当调用无参构造方法，返回的是该数组。刚创建一个ArrayList 时，其内数据量为0。
     * 它与EMPTY_ELEMENTDATA的区别就是：该数组是默认返回的，而后者是在用户指定容量为0时返回。
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 保存添加到ArrayList中的元素。
     * ArrayList的容量就是该数组的长度。
     * 该值为DEFAULTCAPACITY_EMPTY_ELEMENTDATA 时，当第一次添加元素进入ArrayList中时，数组将扩容值DEFAULT_CAPACITY。
     * 被标记为transient，在对象被序列化的时候不会被序列化。
     * 底层数组，ArrayList中真正存储元素的地方
     */
    transient Object[] elementData;

    /**
     * ArrayList的实际大小（数组包含的元素个数）。
     *
     * @serial
     */
    private int size;

    /**
     * 构造一个指定初始化容量为capacity的空ArrayList。
     *
     * @param initialCapacity 初始化容量
     * @throws IllegalArgumentException 如果ArrayList的指定初始化容量为负。
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 如果大于零的话，就把底层的elementData进行初始化为指定容量的数组。
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 如果我们给定的容量等于零，它就会调用上面的空数组 EMPTY_ELEMENTDATA。
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            //如果小于零的话，就抛出了违法参数异常（IllegalArgumentException）。
            throw new IllegalArgumentException("集合初始化参数异常" + initialCapacity);
        }
    }

    /**
     * 空无参数构造
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 传入一个集合，首先把集合转化为数组，然后把集合的底层数组elementData指向该数组，
     * 此时，底层数组有元素了，而size属性表示ArrayList内部元素的个数，所以需要把底层数组
     * element的大小赋值给size属性，然后在它不等于0 的情况
     * 下（也就是传进来的集合不为空），再通过判断保证此刻底层数组elementData数组的类型
     * 和Object[]类型相同，如果不同，则拷贝一个Object[]类型的数组给elementData数组。
     * 如果参数collection为null的话，将会报空指针异常。
     *
     * @param c 一个Collection集合。
     * @throws NullPointerException，将会报异常（NullPointerException）
     */
    public ArrayList(Collection<? extends E> c) {
        //此处会空指针异常
        //传入一个集合，首先把集合转化为数组，然后把集合的底层数组elementData指向该数组，
        this.elementData = c.toArray();
        // element的大小赋值给size属性
        size = elementData.length;
        if (size != 0) {
            if (elementData.getClass() != Object.class) {
                elementData = Arrays.copyOf(elementData, size, Object[].class);
            }
        } else {
            //指定该ArrayList容量为0时，返回该空数组。
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * 把ArrayList的底层elementData数组大小调整为size（size是ArrayList集合中存储的元素的个数）
     * 因为我们在ArrayList中添加元素的时候，当ArrayList容量不足的时候，ArrayList会自动扩容，
     * （调用的是ensureCapacityInternal()方法
     * 一般扩充为原来容量的1.5倍，我们可能用不了那么多的空间，所以，有时需要这个来节省空间。
     */
    public void trimToSize() {
        //modCount这个变量从字面意思看，它代表了修改的次数。
        //它是AbstractList中的 protected修饰的字段。
        //追根溯源还是由于ArrayList是一个线程不安全的类。这个变量主要是用来保证在多线程环境下使用
        //迭代器的时候，同时在对集合做修改操作时，同一时刻只能有一个线程修改集合，如果多于一个
        //线程进行对集合改变的操作时，就会抛出ConcurrentModificationException。
        modCount++;
        //接着判断一下，如果ArrayList中元素的个数小于底层数组的长度，说明此时需要缩容。
        if (size < elementData.length) {
            //如果ArrayList中没有元素，则把底层数组设置为空数组。
            //否则的话，就使用数组拷贝把底层数组的空间大小缩为size（元素个数）的大小。
            elementData = size == 0 ? EMPTY_ELEMENTDATA : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * 官方的JDK中首先：需要确定一个最小的预期容量（minCapacity）：
     * 它通过判断底层数组是否是DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * 如果没有使用默认的构造器的话，它的最小预期容量是0，
     * 如果使用了默认构造器，最小预期容量（minCapacity）为默认容量（DEFAULT_CAPACITY：10），
     * 最后判断一下，如果参数大于最小预期的话，则需要调用ensureExplicitCapacity()方法扩容。
     *
     * @param minCapacity
     */
    public void ensureCapacity(int minCapacity) {
        //判断最小 0还是默认10
        int minExpand = elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA ? 0 : DEFAULT_CAPACITY;
        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    /**
     * @param minCapacity 最小孔容
     */
    private void ensureExplicitCapacity(int minCapacity) {
        //因为需要进行扩容，也就是ArrayList发生了变化，所以需要modCount++.
        modCount++;
        //接着判断一下，如果我们传进来的参数（需要扩充的容量）大于底层数组的长度elemntData的时候，就需要扩容了。 扩容见下面的grow（）方法。
        if (minCapacity - elementData.length > 0) {
            grow(minCapacity);
        }
    }

    /**
     * 开始扩容
     *
     * @param minCapacity 最小孔容
     */
    private void grow(int minCapacity) {
        //首先是新建一个变量oldCapacity，把底层数组的长度保存起来，然后通过oldCapacity做
        int oldCapacity = elementData.length;
        //移位运算，向右移移位，就变成了oldCapacity的1.5倍了
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        //接着判断一下，如果newCapacity（它是底层数组容量的1.5倍）的大小仍然小于我们
        if (newCapacity - minCapacity < 0) {
            //自定义传进来的参数minCapacity的大小，就把minCapacity的值赋值给newCapacity。
            newCapacity = minCapacity;
        }
        //接着再判断一下如果newCapacity的大小超过了最大数组容量（MAX_ARRAY_SIZE），
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        //如果超出MAX_ARRAY_SIZE,会调用该方法分配Integer.MAX_VALUE的空间
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    /**
     * 集合长度
     *
     * @return 长度
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 判断ArrayList是否为空：
     *
     * @return 判断ArrayList是否为空：
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @param o AbstractList中是使用迭代器完成的
     * @return 是否包含该元素
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * 获取元素位置 从前往后
     *
     * @param o 元素
     * @return 结果
     */
    @Override
    public int indexOf(Object o) {
        //判断元素是否为空
        if (o == null) {
            //判断集合中是否有空元素
            for (int i = 0; i < size; i++)
                if (elementData[i] == null)
                    return i;
        } else {
            //判断属性是否相同
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 获取元素位置 从后往前
     *
     * @param o 元素
     * @return 结果
     */
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (elementData[i] == null)
                    return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            //直接调用超类Object的clone方法，然后强制转化一下类型
            ArrayList<?> v = (ArrayList<?>) super.clone();
            //然后把底层数组拷贝一下，
            v.elementData = Arrays.copyOf(elementData, size);
            // 切记要将modCount设置为0，最后返回即可。
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     * @return 数组转换
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * 首先判断一下传进来的数组a的大小是否小于需要转化为数组的ArrayList的数组元素的个数，如果小于的话，返回原来ArrayList底层存储元素的数组elementData，并且它的大小设置为ArrayList中元素的个数，且类型为传进来的类型参数a。（传进来的参数和返回值类型必须一致）
     * 如果传进来数组a的带下不小于ArrayList中元素的个数的话，则先把底层数组elementData中的元素全部复制到a中。
     * 接下来比较奇怪的一点是：如果a的长度大于size的话，则把a[size]设置为空。
     * 我通过代码测试和查看官方解释得出如下结论：
     * 如果数组a的length大于ArrayList中元素的个数size，则把a[size]置为空，从结果目的是为了区分ArrayList和转化后的数组的大小以及内容有哪些区别。
     * 如果是等于的话，它和小于的结果显示一样。都是输出了转化后的数组，且元素和元素的个数与ArrayList中元素和元素个数完全一致。
     */
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    E elementData(int index) {
        //查看底层elementData数组的某个元素：（直接通过数组访问）
        return (E) elementData[index];
    }

    private void rangeCheck(int index) {
        //检查底层数组是否越界：
        if (index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    public E get(int index) {
        rangeCheck(index);
        //获取某个位置上的元素：（直接通过数组操作）
        return elementData(index);
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    public E set(int index, E element) {
        rangeCheck(index);

        E oldValue = elementData(index);
        //直接通过数组操作
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 添加一个元素之前，需要先判断一下容量（底层elementData数组的大小是否满足条件），
     * 如果容量不足， 足需要扩容，然后扩大后的容量取决于size+1，
     * 最后给elementData[size++]赋值就可以了，最后返回true。
     *
     * @param e 元素
     * @return 是否添加成功
     */
    public boolean add(E e) {
        //传入当前ArrayList中元素的个数+1； 此步骤为了怕段是否需要扩容和扩容使用
        ensureCapacityInternal(size + 1);
        //赋值
        elementData[size++] = e;
        return true;
    }

    private void ensureCapacityInternal(int minCapacity) {
        minCapacity = calculateCapacity(elementData, minCapacity);
        //接着调用ensureExplicitCapacity(int minCapacity）方法，
        // 如果minCapacity大于底层数组的长度，则需要调用grow(minCapacity)进行扩容，
        // 否则的话，add（）方法中的ensureCapacityInternal(size + 1)什么也不做。
        // （其实ensureCapacityInternal(size + 1)方法就是为了判断数组用不用扩容。）
        ensureExplicitCapacity(minCapacity);
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        //如果我们的ArrayList是通过默认的构造器创建的话
        // 则把minCapacity（也就是我们的size+1）和DEFAULT_CAPACITY（默认容量是10）比较，取较大者返回，否则的话，就返回minCapacity，
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    private void rangeCheckForAdd(int index) {
        //为进行增加操作的方法添加范围检查。
        //如果index大于size或者小于0，则抛出IndexOutOfBoundsException。
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    public void add(int index, E element) {
        //为进行增加操作的方法添加范围检查。
        rangeCheckForAdd(index);
        //是否需要扩容
        ensureCapacityInternal(size + 1);
        //数组拷贝
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        //赋值
        elementData[index] = element;
        //集合长度计算
        size++;
    }

    /**
     * 在移动元素的时候，仍然选择进行数组的拷贝。
     * 但是，这里涉及到一个数组边界的问题：
     * 我们在计算出要移动的元素的个数后（
     * 这里可能有的童鞋看不懂为什么移动的个数是size-index-1，你可以画个图一下子就出来了，
     * 其实就是因为我们的index指代的是数组的下标，所以index位置处的元素以及它前面的元素的和是index+1，
     * 所以剩下的元素就是size-index-1，而剩下的元素，也就是index位置后的所有元素都需要向前移动，
     * 所以numMoved=size-index-1），
     * 移动的元素的个数大于0，我们下面数组拷贝
     * （其实就是起到了移动元素的功能）才有意义，
     * 否则的话直接在elementData[–size]处设置为空就可以了。记住最后需要返回移除了的值。
     * @param index
     * @return
     */
    public E remove(int index) {
        //索引检查
        rangeCheck(index);
        //次数统计
        modCount++;
        //获取当前元素
        E oldValue = elementData(index);
        //获取移动坐标
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            //开始移动
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        }
        //最后一个元素滞空 集合长度减一
        elementData[--size] = null;
        //移除成功
        return oldValue;
    }

    /**
     * 获取元素位置后快速删除该元素
     * @param o 元素
     * @return 只会删除一个？
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /**
     * 这个是JDK提供的删除指定位置上元素的更快的方法 remove(int index) 类似
     * @param index 坐标
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null;
    }

    public void clear() {
        //操作集合
        modCount++;
        for (int i = 0; i < size; i++)
            //从代码中看，非常简单，通过for循环把底层数组中的元素全部置为null，
            elementData[i] = null;
        // 然后把size设置为0就可以了
        size = 0;
    }

    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        //检查扩容
        ensureCapacityInternal(size + numNew);
        //填充数据
        System.arraycopy(a, 0, elementData, size, numNew);
        //统计集合长度
        size += numNew;
        //所有元素是否添加成功
        return numNew != 0;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        //坐标检查
        rangeCheckForAdd(index);
        //新添加的数组
        Object[] a = c.toArray();
        //追加的长度
        int numNew = a.length;
        //是否扩容 包括自动扩容
        ensureCapacityInternal(size + numNew);
        //索引是否正确
        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);
        //拷贝数组
        System.arraycopy(a, 0, elementData, index, numNew);
        //设置集合长度
        size += numNew;
        //是否全部添加完毕
        return numNew != 0;
    }

    /**
     * 移除某一个范围的元素：
     * @param fromIndex 开始
     * @param toIndex 结束
     */
    protected void removeRange(int fromIndex, int toIndex) {
        //变更统计
        modCount++;
        //需要保留的元素
        int numMoved = size - toIndex;
        //拷贝元素
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                numMoved);
        //长度设置
        int newSize = size - (toIndex-fromIndex);
        //elementData 集合外元素设置为空
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        //设置集合长度
        size = newSize;
    }

    /**
     * 移除集合中包含的所有元素：
     * @param c 集合
     * @return 结果
     */
    public boolean removeAll(Collection<?> c) {
        //首先需要调用Objects类保证参数不为空
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            if (r != size) {
                System.arraycopy(elementData, r,
                        elementData, w,
                        size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

}

