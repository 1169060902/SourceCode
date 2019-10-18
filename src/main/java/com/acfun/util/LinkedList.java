//package com.acfun.util;
//
//import java.util.*;
//
///**
// * @param <E> 支持泛型
// *            AbstractSequentialList 在遍历LinkedList的时候，官方更推荐使用顺序访问，也就是使用我们的迭代器。
// *            List 实现了List接口。（提供List接口中所有方法的实现）
// *            Deque 实现了Deque接口。实现了Deque所有的可选的操作
// *            Cloneable 实现了Cloneable接口，它支持克隆（浅克隆），底层实现：LinkedList节点并没有被克隆，只是通过Object的clone（）方法得到的Object对象强制转化为了LinkedList,然后把它内部的实例域都置空，然后把被拷贝的LinkedList节点中的每一个值都拷贝到clone中。
// *            Serializable 表明它支持序列化。（和ArrayList一样，底层都提供了两个方法：readObject（ObjectInputStream o）、writeObject（ObjectOutputStream o），用于实现序列化，底层只序列化节点的个数和节点的值）。
// */
//public class LinkedList<E>
//        extends AbstractSequentialList<E>
//        implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
//
//    /**
//     * 集合长度 用来记录LinkedList的大小
//     */
//    transient int size = 0;
//    /**
//     * 第一个节点值
//     * 用来表示LinkedList的头节点。
//     */
//    transient Node<E> first;
//    /**
//     * 最后一个节点值
//     * 用来表示LinkedList的尾节点。
//     */
//    transient Node<E> last;
//
//    /**
//     * LinkedList提供了两个构造器，ArrayList比它多提供了一个通过设置初始化容量来初始化类。
//     * LinkedList不提供该方法的原因：因为LinkedList底层是通过链表实现的，每当有新元素添加进来的时候，都是通过链接新的节点实现的，
//     * 也就是说它的容量是随着元素的个数的变化而动态变化的。
//     * 而ArrayList底层是通过数组来存储新添加的元素的，所以我们可以为ArrayList设置初始容量（实际设置的数组的大小）。
//     */
//    public LinkedList() {
//
//    }
//
//    public LinkedList(Collection<? extends E> c) {
//        //首先调用一下空的构造器。
//        this();
//        //然后调用addAll(c)方法。 开始赋值
//        addAll(c);
//    }
//
//    public boolean addAll(Collection<? extends E> c) {
//        //添加元素会修改集合长度 而且需要起始添加位置 默认当前最后一个节点索引
//        return addAll(size, c);
//    }
//
//    public boolean addAll(int index, Collection<? extends E> c) {
//        //几乎所有的涉及到在指定位置添加或者删除或修改操作都需要判断传进来的参数是否合法。
//        checkPositionIndex(index);
//        Object[] a = c.toArray();
//        int numNew = a.length;
//        if (numNew == 0) {
//            return false;
//        }
//        //Node<E> succ：指代待添加节点的位置。
//        //Node<E> pred：指代待添加节点的前一个节点。
//        Node<E> pred, succ;
//        //最后位置添加
//        if (index == size) {
//            succ = null;
//            pred = last;
//        } else {
//            //指定位置添加
//            //获取指定位置的元素
//            succ = node(index);
//            //获取指定位置元素前一个元素
//            pred = succ.prev;
//        }
//        for (Object o : a) {
//            //元素转换
//            E e = (E) o;
//            //开始添加元素 前一个元素 本元素 后一个元素
//            Node<E> newNode = new Node<>(pred, e, null);
//            //判断前一个元素 如果前一个元素为空 则代表当前元素为第一个元素
//            if (pred == null) {
//                first = newNode;
//            } else {
//                //当前元素为最后一个
//                last = newNode;
//            }
//            //开始下一轮添加元素 此时上一个元素为当前元素 如果是最后一个循环则代表最后一个元素
//            pred = newNode;
//        }
//        //也就是新添加的节点位于LinkedList集合的最后一个元素的后面
//        if (succ == null) {
//            //last指向pred指向的节点。
//            last = pred;
//        } else {
//            //succ != null 则代表是中间插入元素
//            //插入的最后一个元素下一个元素指向插入点元素
//            pred.next = succ;
//            //插入点元素上一个元素 指向最后一个插入的元素
//            succ.prev = pred;
//        }
//        //最后把集合的大小设置为新的大小。
//        size += numNew;
//        //modCount（修改的次数）自增。
//        modCount++;
//        return true;
//    }
//
//    /**
//     * 获取指定节点
//     *
//     * @param index 坐标
//     * @return 指定节点
//     */
//    Node<E> node(int index) {
//        //判断index是否小于size的一半，如果小于则从头遍历节点，否则从结尾遍历节点 此处是为了效率 正向或者逆向
//        if (index < (size >> 1)) {
//            //正向
//            Node<E> x = first;
//            //遍历获取当前坐标值
//            for (int i = 0; i < index; i++)
//                x = x.next;
//            return x;
//        } else {
//            //逆向
//            Node<E> x = last;
//            //遍历获取当前坐标值
//            for (int i = size - 1; i > index; i--) {
//                x = x.prev;
//            }
//            return x;
//        }
//    }
//
//    /**
//     * 判断索引是否越界
//     *
//     * @param index 索引
//     */
//    private void checkPositionIndex(int index) {
//        if (!isPositionIndex(index))
//            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
//    }
//
//    private String outOfBoundsMsg(int index) {
//        return "Index: " + index + ", Size: " + size;
//    }
//
//    private boolean isPositionIndex(int index) {
//        return index >= 0 && index <= size;
//    }
//
//
//    /**
//     * 内部类 代表个节点数据
//     * Node 类是LinkedList中的私有内部类，LinkedList中就是通过Node来存储集合中的元素。
//     *
//     * @param <E>
//     */
//    private static class Node<E> {
//        //E ：节点的值。
//        E item;
//        //当前节点的后一个节点的引用（可以理解为指向当前节点的后一个节点的指针）
//        Node<E> next;
//        //当前节点的前一个节点的引用（可以理解为指向当前节点的前一个节点的指针）
//        Node<E> prev;
//
//        //提供构造关联个节点数据
//        Node(Node<E> prev, E element, Node<E> next) {
//            this.item = element;
//            this.next = next;
//            this.prev = prev;
//        }
//
//    }
//
//    /**
//     * 节点数据滞空
//     */
//    public void clear() {
//        for (Node<E> x = first; x != null; ) {
//            Node<E> next = x.next;
//            x.item = null;
//            x.next = null;
//            x.prev = null;
//            x = next;
//        }
//        first = last = null;
//        size = 0;
//        modCount++;
//    }
//
//    /**
//     * 获取指定元素值
//     * @param index 索引
//     * @return 值
//     */
//    public E get(int index) {
//        checkElementIndex(index);
//        return node(index).item;
//    }
//
//    /**
//     * 检索索引
//     * @param index 坐标
//     */
//    private void checkElementIndex(int index) {
//        if (!isElementIndex(index))
//            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
//    }
//
//    private boolean isElementIndex(int index) {
//        return index >= 0 && index < size;
//    }
//
//    @Override
//    public ListIterator<E> listIterator(int index) {
//        return null;
//    }
//
//    @Override
//    public void addFirst(E e) {
//
//    }
//
//    @Override
//    public void addLast(E e) {
//
//    }
//
//    @Override
//    public boolean offerFirst(E e) {
//        return false;
//    }
//
//    @Override
//    public boolean offerLast(E e) {
//        return false;
//    }
//
//    @Override
//    public E removeFirst() {
//        return null;
//    }
//
//    @Override
//    public E removeLast() {
//        return null;
//    }
//
//    @Override
//    public E pollFirst() {
//        return null;
//    }
//
//    @Override
//    public E pollLast() {
//        return null;
//    }
//
//    @Override
//    public E getFirst() {
//        return null;
//    }
//
//    @Override
//    public E getLast() {
//        return null;
//    }
//
//    @Override
//    public E peekFirst() {
//        return null;
//    }
//
//    @Override
//    public E peekLast() {
//        return null;
//    }
//
//    @Override
//    public boolean removeFirstOccurrence(Object o) {
//        return false;
//    }
//
//    @Override
//    public boolean removeLastOccurrence(Object o) {
//        return false;
//    }
//
//    @Override
//    public boolean offer(E e) {
//        return false;
//    }
//
//    @Override
//    public E remove() {
//        return null;
//    }
//
//    @Override
//    public E poll() {
//        return null;
//    }
//
//    @Override
//    public E element() {
//        return null;
//    }
//
//    @Override
//    public E peek() {
//        return null;
//    }
//
//    @Override
//    public void push(E e) {
//
//    }
//
//    @Override
//    public E pop() {
//        return null;
//    }
//
//    @Override
//    public int size() {
//        return 0;
//    }
//
//    @Override
//    public Iterator<E> descendingIterator() {
//        return null;
//    }
//}
