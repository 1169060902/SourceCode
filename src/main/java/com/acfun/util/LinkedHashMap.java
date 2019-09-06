//package com.acfun.util;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *LinkedHashMap继承了HashMap，是Map接口的哈希表和链接列表实现。哈希表的功能通过继承HashMap实现了。
// * LinkedHashMap还维护着一个双重链接链表。此链表定义了迭代顺序，该迭代顺序可以是插入顺序或者是访问顺序。
// * @param <K>
// * @param <V>
// *     HashMap<K,V> 实现了继承了HashMap的特点
// *
// */
//public class LinkedHashMap<K,V>
//        extends HashMap<K,V>
//        implements Map<K,V> {
//
//
//
//    /**
//     * The head (eldest) of the doubly linked list.
//     * 双向循环链表的头结点
//     */
//    transient LinkedHashMap.Entry<K,V> head;
//    /**
//     * The tail (youngest) of the doubly linked list.
//     * 双向循环链表的尾结点
//     */
//    transient LinkedHashMap.Entry<K,V> tail;
//
//    /**
//     * The iteration ordering method for this linked hash map: <tt>true</tt>
//     * for access-order, <tt>false</tt> for insertion-order.
//     *   * 迭代顺序。
//     *  * true代表按访问顺序迭代
//     *  * false代表按插入顺序迭代
//     * @serial
//     */
//    final boolean accessOrder;
//
//    /**
//     * 将指定entry插入到双向链表末尾
//     * @param p
//     */
//    private void linkNodeLast(LinkedHashMap.Entry<K,V> p) {
//        LinkedHashMap.Entry<K,V> last = tail;
//        //尾指针执行p
//        tail = p;
//        //如果旧的尾节点指向null，意味着双向循环链表为空，这时头尾指针都要指向p
//        if (last == null)
//            head = p;
//        else {
//            //否则将p插入到旧尾节点的后面
//            p.before = last;
//            last.after = p;
//        }
//    }
//
//    /**
//     * 将src替换为dst
//     * @param src
//     * @param dst
//     */
//    private void transferLinks(LinkedHashMap.Entry<K,V> src,
//                               LinkedHashMap.Entry<K,V> dst) {
//        LinkedHashMap.Entry<K,V> b = dst.before = src.before;
//        LinkedHashMap.Entry<K,V> a = dst.after = src.after;
//        //如果src的前指针指向null，说明src为头节点，这时将dst替换为头节点即可
//        if (b == null)
//            head = dst;
//        else//否则，将dst的前指针指向的节点的后指针指向dst
//            b.after = dst;
//        //如果src的后指针指向null，说明src为尾节点，这时将dst替换为尾节点即可
//        if (a == null)
//            tail = dst;
//        else//否则，将dst的后指针指向的节点的前指针指向dst
//            a.before = dst;
//    }
//
//    //将linkedHashMap重置到初始化的默认状态
//    void reinitialize() {
//        //重置哈希表
//        super.reinitialize();
//        //重置双向循环链表
//        head = tail = null;
//    }
//
//    /**
//     * 创建一个普通entry，将entry插入到双向循环链表的末尾,最后返回entry
//     */
//    Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
//        //创建一个entry
//        LinkedHashMap.Entry<K,V> p =
//                new LinkedHashMap.Entry<K,V>(hash, key, value, e);
//        //将entry插入到双向循环链表的末尾
//        linkNodeLast(p);
//        //返回entry
//        return p;
//    }
//
//    /**
//     * 替换普通节点
//     */
//    Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
//        LinkedHashMap.Entry<K,V> q = (LinkedHashMap.Entry<K,V>)p;
//        LinkedHashMap.Entry<K,V> t =
//                new LinkedHashMap.Entry<K,V>(q.hash, q.key, q.value, next);
//        transferLinks(q, t);
//        return t;
//    }
//
//    /**
//     * 创建一个树的entry，将entry插入到双向循环链表的末尾,最后返回entry
//     */
//    TreeNode<K,V> newTreeNode(int hash, K key, V value, Node<K,V> next) {
//        TreeNode<K,V> p = new TreeNode<K,V>(hash, key, value, next);
//        linkNodeLast(p);
//        return p;
//    }
//
//
//
//
//    /**
//     * 重写 节点 和父类节点一致
//     * @param <K>
//     * @param <V>
//     */
//    static class Entry<K,V> extends HashMap.Node<K,V> {
//        Entry<K,V> before, after;
//        Entry(int hash, K key, V value, Node<K,V> next) {
//            super(hash, key, value, next);
//        }
//    }
//}
