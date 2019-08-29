//package com.acfun.util;
//
//import java.io.Serializable;
//import java.util.*;
//
///**
// * @param <K> key 允许为null
// * @param <V> value 运行为null
// *            AbstractMap HashMap直接继承自AbstractMap抽象类，在AbstractMap已经实现了Map接口中的所有方法，只有public abstract Set<Entry<K,V>> entrySet()方法是抽象
// *            Map 接口
// *            Cloneable 支持克隆
// *            Serializable 支持序列化
// */
//public class HashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
//    /**
//     * HashMap对应的序列化ID。
//     */
//    private static final long serialVersionUID = 362498820763181265L;
//    /**
//     * HashMap的哈希桶数组默认的初始化容量（2的4次方（也就是16））
//     */
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
//    /**
//     * HashMap的哈希桶数组的最大容量（2的30次方）
//     */
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//    /**
//     * 默认的加载因子
//     */
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//    /**
//     * HashMap中一个桶的树化的阀值（一旦在同一个哈希桶中的元素超过8个 链表就会变成红黑树）
//     */
//    static final int TREEIFY_THRESHOLD = 8;
//    /**
//     * HashMap中一个桶的链表化的阀值（一旦同一个哈希桶中元素少于6个，红黑树就变成了链表）
//     */
//    static final int UNTREEIFY_THRESHOLD = 6;
//    /**
//     * 当哈希桶的容量大于这个值时，表中的桶才能进行树形化
//     */
//    static final int MIN_TREEIFY_CAPACITY = 64;
//    /**
//     * HashMap中键值对的个数
//     */
//    transient int size;
//    /**
//     * *HashMap被修改的次数（主要用于快速失败（ArrayList和LinkedList等线程不安全的类都用到了这个变量））
//     */
//    transient int modCount;
//
//    /**
//     * //HashMap中实际允许存储的键值对的最大数量
//     * //（一旦超过这个值，表明哈系冲突很严重了，就需要扩容了。）
//     * //threshold = table.length*loadFactor（也就是哈希桶数组的长度*加载因子）
//     */
//    int threshold;
//    /**
//     * 哈系桶数组
//     */
//    transient Node<K, V>[] table;
//    /**
//     * HashMap中的键值对缓存在entrySet中，即使key在外部修改导致hashCode变化，该缓存中
//     * 仍然可以找到映射关系。
//     */
//    transient Set<Map.Entry<K, V>> entrySet;
//
//    /**
//     * 加载因子(默认值是0.75)
//     */
//    final float loadFactor;
//
//    /**
//     * 无参数构造
//     */
//    public HashMap() {
//        //加载因子(默认值是0.75)
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//    }
//
//    /**
//     * @param initialCapacity 初始容量
//     * @param loadFactor      加载因子
//     */
//    public HashMap(int initialCapacity, float loadFactor) {
//        if (initialCapacity < 0)
//            //初始容量小于0 异常
//            throw new IllegalArgumentException("Illegal initial capacity: " +
//                    initialCapacity);
//        //初始容量大于 1 << 30 HashMap的哈希桶数组的最大容量（2的30次方）
//        if (initialCapacity > MAXIMUM_CAPACITY)
//            initialCapacity = MAXIMUM_CAPACITY;
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            //加载因子 必须存在
//            throw new IllegalArgumentException("Illegal load factor: " +
//                    loadFactor);
//        //加载因子 赋值
//        this.loadFactor = loadFactor;
//        //HashMap中实际允许存储的键值对的最大数量
//        this.threshold = tableSizeFor(initialCapacity);
//    }
//
//    /**
//     * 此处只会输出 2的n 次方
//     *
//     * @param cap 初始容量
//     * @return 输出结果
//     */
//    static final int tableSizeFor(int cap) {
//        int n = cap - 1;
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//    }
//
//
//    /**
//     * 初始容量构造
//     *
//     * @param initialCapacity 初始容量
//     */
//    public HashMap(int initialCapacity) {
//        //初始容量 默认加载因子 0.75
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    /**
//     * 计算hash值
//     *
//     * @param key key
//     * @return 结果
//     */
//    static final int hash(Object key) {
//        int h;
//        //先获取到key的hashCode，然后进行移位再进行异或运算，为什么这么复杂，不用想肯定是为了减少hash冲突
//        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//    }
//
//    /**
//     * 传入一个map 转HashMap
//     *
//     * @param m map
//     */
//    public HashMap(Map<? extends K, ? extends V> m) {
//        //加载因子
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//        putMapEntries(m, false);
//    }
//
//    /**
//     * 当是初始化构造map的时候，evict为false，如果已经初始化完成后，evict为true
//     *
//     * @param m     传入一个map
//     * @param evict 如果已经初始化完成后，evict为true
//     */
//    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
//        //把map实际存储键值对的大小用一个变量s存储起来。
//        int s = m.size();
//        //如果s大于零（键值对的个数大于0）
//        if (s > 0) {
//            //如果HashMap中哈系桶数组为空
//            if (table == null) {
//                //根据m的键值对的数量和HashMap的装载因子计算阀值。
//                float ft = ((float) s / loadFactor) + 1.0F;
//                //限制阀值不能超过MAXIMUM_CAPACITY
//                int t = ((ft < (float) MAXIMUM_CAPACITY) ?
//                        (int) ft : MAXIMUM_CAPACITY);
//                if (t > threshold)
//                    //HashMap的阀值。
//                    threshold = tableSizeFor(t);
//            } else if (s > threshold)
//                //扩容和初始化
//                resize();
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
//                K key = e.getKey();
//                V value = e.getValue();
//                putVal(hash(key), key, value, false, evict);
//            }
//        }
//    }
//
//    /**
//     * 插入值
//     */
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
//                   boolean evict) {
//        Node<K,V>[] tab; Node<K,V> p; int n, i;
//        if ((tab = table) == null || (n = tab.length) == 0)
//            //会对该桶进行第一次初始化，桶的数组大小为16
//            n = (tab = resize()).length;
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            //判断桶的下标是否含有第一个元素，没有的话就放进去
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            //桶的下标已经存在第一个元素了
//            Node<K,V> e; K k;
//            //判断桶下标中存在的第一个元素的hash值和key值是否相等
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                //相等的话则用e来进行记录
//                e = p;
//            else if (p instanceof TreeNode)
//                //hash值相等，key不相等则判断标中存在的第一个元素是否为树的节点
//                //是的话则将元素添加到树节点上
//                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//                //hash值相等，key不相等放到链表中
//            else {
//                for (int binCount = 0; ; ++binCount) {
//                    //判断该链表尾部指针是不是空的
//                    if ((e = p.next) == null) {
//                        //在链表的尾部创建链表节点
//                        p.next = newNode(hash, key, value, null);
//                        //判断链表的长度是否达到转化红黑树的临界值，临界值为8
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            //链表结构转树形结构
//                            treeifyBin(tab, hash);
//                        break;
//                    }
//                    //判断链表中的节点是否与该节点相等
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//                    p = e;
//                }
//            }
//            //判断当前的key已经存在的情况下，再来一个相同的hash值、key值时返回新来的value这个值
//            if (e != null) { // existing mapping for key
//                V oldValue = e.value;
//                if (!onlyIfAbsent || oldValue == null)
//                    e.value = value;
//                afterNodeAccess(e);
//                return oldValue;
//            }
//        }
//        ++modCount;
//        //直到桶的数组大小超过了负载的临界值时，则进行扩容
//        if (++size > threshold)
//            resize();
//        afterNodeInsertion(evict);
//        return null;
//    }
//
//
//    /**
//     * 获取key对应的值，如果找不到则返回null
//     * 但是如果返回null并不意味着就没有找到，也可能key对应的值就是null，因为HashMap允许null值（也允许null键）
//     * 在返回值为null时，可以通过containsKey来方法来区分到底是因为key不存在，还是key对应的值就位null
//     */
//    public V get(Object key) {
//        Node<K,V> e; // 声明一个节点对象（键值对对象）
//        // 调用getNode方法来获取键值对，如果没有找到返回null，找到了就返回键值对的值
//        return (e = getNode(hash(key), key)) == null ? null : e.value; //真正的查找过程都是通过getNode方法实现的
//    }
//
//    /**
//     * 检查是否包含key
//     * 如果key有对应的节点对象，则返回ture，不关心节点对象的值是否为空
//     */
//    public boolean containsKey(Object key) {
//        // 调用getNode方法来获取键值对，如果没有找到返回false，找到了就返回ture
//        return getNode(hash(key), key) != null; //真正的查找过程都是通过getNode方法实现的
//    }
//
//    /**
//     * 该方法是Map.get方法的具体实现
//     * 接收两个参数
//     * @param hash key的hash值，根据hash值在节点数组中寻址，该hash值是通过hash(key)得到的，可参见：hash方法解析
//     * @param key key对象，当存在hash碰撞时，要逐个比对是否相等
//     * @return 查找到则返回键值对节点对象，否则返回null
//     */
//    final Node<K,V> getNode(int hash, Object key) {
//        Node<K,V>[] tab; //声明节点数组对象
//        Node<K,V> first, e; //链表的第一个节点对象、循环遍历时的当前节点对象
//        int n; //数组长度
//        K k; // 、、、节点的键对象
//        // 节点数组赋值、数组长度赋值、通过位运算得到求模结果确定链表的首节点
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (first = tab[(n - 1) & hash]) != null) {
//            if (first.hash == hash && // 首先比对首节点，如果首节点的hash值和key的hash值相同 并且 首节点的键对象和key相同（地址相同或equals相等），则返回该节点
//                    ((k = first.key) == key || (key != null && key.equals(k))))
//                return first; // 返回首节点
//
//            // 如果首节点比对不相同、那么看看是否存在下一个节点，如果存在的话，可以继续比对，如果不存在就意味着key没有匹配的键值对
//            if ((e = first.next) != null) {
//                // 如果存在下一个节点 e，那么先看看这个首节点是否是个树节点
//                if (first instanceof TreeNode)
//                    // 如果是首节点是树节点，那么遍历树来查找
//                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
//
//                // 如果首节点不是树节点，就说明还是个普通的链表，那么逐个遍历比对即可
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k)))) // 比对时还是先看hash值是否相同、再看地址或equals
//                        return e; // 如果当前节点e的键对象和key相同，那么返回e
//                } while ((e = e.next) != null); // 看看是否还有下一个节点，如果有，继续下一轮比对，否则跳出循环
//            }
//        }
//        return null; // 在比对完了应该比对的树节点 或者全部的链表节点 都没能匹配到key，那么就返回null
//    }
//
//    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
//        return new Node<>(hash, key, value, next);
//    }
//
//    /**
//     * 扩容兼初始化
//     *
//     * @return 扩容后节点
//     */
//    final Node<K, V>[] resize() {
//        //旧
//        Node<K, V>[] oldTab = table;
//        //数组长度
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        //临界值
//        int oldThr = threshold;
//        //新数组长度 新临界值
//        int newCap, newThr = 0;
//        if (oldCap > 0) {
//            //旧数组长度大于0 表示有数据
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                // 原数组长度大于最大容量(1073741824) 则将threshold设为Integer.MAX_VALUE=2147483647
//                // 接近MAXIMUM_CAPACITY的两倍
//                threshold = Integer.MAX_VALUE;
//                //扩容完成
//                return oldTab;
//            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
//                // 新数组长度 是原来的2倍，
//                // 临界值也扩大为原来2倍
//                newThr = oldThr << 1;
//            }
//        } else if (oldThr > 0) {
//            //如果原来的thredshold大于0则将容量设为原来的thredshold
//            //在第一次带参数初始化时候会有这种情况
//            newCap = oldThr;
//        } else {
//            // 在默认无参数初始化会有这种情况 初始容量16
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            //初始     0.75*16=12
//            // 在默认无参数初始化会有这种情况
//            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        if (newThr == 0) {
//            // 如果新 的容量 ==0
//            float ft = (float) newCap * loadFactor;
//            //loadFactor 哈希加载因子 默认0.75,可在初始化时传入,16*0.75=12 可以放12个键值对
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
//                    (int) ft : Integer.MAX_VALUE);
//        }
//        //将临界值设置为新临界值
//        threshold = newThr;
//        // 扩容
//        @SuppressWarnings({"rawtypes", "unchecked"})
//        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
//        table = newTab;
//        // 如果原来的table有数据，则将数据复制到新的table中
//        if (oldTab != null) {
//            // 根据容量进行循环整个数组，将非空元素进行复制
//            for (int j = 0; j < oldCap; ++j) {
//                Node<K, V> e;
//                // 获取数组的第j个元素
//                if ((e = oldTab[j]) != null) {
//                    oldTab[j] = null;
//                    // 如果链表只有一个，则进行直接赋值
//                    if (e.next == null)
//                        // e.hash & (newCap - 1) 确定元素存放位置
//                        newTab[e.hash & (newCap - 1)] = e;
//                    else if (e instanceof TreeNode)
//                        //如果原来这个节点已经转化为红黑树了，
//                        //那么我们去将树上的节点rehash之后根据hash值放到新地方
//                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
//                    else { // preserve order
//                        // 进行链表复制
//                        // 方法比较特殊： 它并没有重新计算元素在数组中的位置
//                        // 而是采用了 原始位置加原数组长度的方法计算得到位置
//                        Node<K, V> loHead = null, loTail = null;
//                        Node<K, V> hiHead = null, hiTail = null;
//                        Node<K, V> next;
//                        do {
//                            next = e.next;
//                            if ((e.hash & oldCap) == 0) {
//                                if (loTail == null)
//                                    loHead = e;
//                                else
//                                    loTail.next = e;
//                                loTail = e;
//                            } else {
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                        } while ((e = next) != null);
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//
//    /**
//     * 链表
//     *
//     * @param <K>
//     * @param <V>
//     */
//    static class Node<K, V> implements Map.Entry<K, V> {
//        //不可变hash 值
//        final int hash;
//        //不可变key
//        final K key;
//        //value
//        V value;
//        //下一个节点
//        Node<K, V> next;
//
//        /**
//         * 构造
//         *
//         * @param hash  值
//         * @param key   key
//         * @param value value
//         * @param next  下一个节点
//         */
//        Node(int hash, K key, V value, Node<K, V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        /**
//         * 该方法实现Map.Entry<K,V>接口。
//         *
//         * @return key
//         */
//        @Override
//        public K getKey() {
//            return key;
//        }
//
//        /**
//         * 该方法实现Map.Entry<K,V>接口。
//         *
//         * @return value
//         */
//        @Override
//        public V getValue() {
//            return value;
//        }
//
//        /**
//         * 该方法实现Map.Entry<K,V>接口。
//         *
//         * @return toString
//         */
//        @Override
//        public final String toString() {
//            return key + "=" + value;
//        }
//
//        /**
//         * 该方法实现Map.Entry<K,V>接口。
//         *
//         * @return hashCode
//         */
//        @Override
//        public final int hashCode() {
//            return Objects.hashCode(key) ^ Objects.hashCode(value);
//        }
//
//        /**
//         * 替换值
//         *
//         * @param newValue 新值
//         * @return 返回旧值
//         */
//        @Override
//        public V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//
//        /**
//         * equals 方法
//         *
//         * @param o 输入对象
//         * @return 判断元素是否相同
//         */
//        public final boolean equals(Object o) {
//            if (o == this) {
//                //同一个地址值 判断相同
//                return true;
//            }
//            //判断是否是元素节点
//            if (o instanceof Map.Entry) {
//                //强转
//                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
//                //判断key value 是否相同相同就一致
//                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue())) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }
//
//    /**
//     * 性质1. 节点是红色或黑色。
//     * 性质2. 根节点是黑色。
//     * 性质3. 每个叶节点（NIL节点，空节点）是黑色的。
//     * 性质4. 每个红色节点的两个子节点都是黑色。(从每个叶子到根的所有路径上不能有两个连续的红色节点)
//     * 性质5. 从任一节点到其每个叶子的路径上包含的黑色节点数量都相同。
//     * 红黑树
//     *
//     * @param <K>
//     * @param <V>
//     */
//    static final class TreeNode<K, V> extends LinkedHashMap.Entry<K, V> {
//        //父节点
//        TreeNode<K, V> parent;
//        //左节点
//        TreeNode<K, V> left;
//        //右节点
//        TreeNode<K, V> right;
//        //前方结点
//        TreeNode<K, V> prev;
//        //是否是红色 非红即黑
//        boolean red;
//
//        /**
//         * 构造方法
//         */
//        TreeNode(int hash, K key, V val, Node<K, V> next) {
//            super(hash, key, val, next);
//        }
//
//        /**
//         * 返回根节点
//         */
//        final TreeNode<K, V> root() {
//            for (TreeNode<K, V> r = this, p; ; ) {
//                //循环获取根节点 父节点为空则代表当前节点为父节点
//                if ((p = r.parent) == null)
//                    return r;
//                r = p;
//            }
//        }
//
//        /**
//         * 把红黑树的根节点设为  其所在的数组槽 的第一个元素
//         * 首先明确：TreeNode既是一个红黑树结构，也是一个双链表结构
//         * 这个方法里做的事情，就是保证树的根节点一定也要成为链表的首节点
//         */
//
//        static <K, V> void moveRootToFront(Node<K, V>[] tab, TreeNode<K, V> root) {
//            int n;
//            // 根节点不为空 并且 HashMap的元素数组不为空
//            if (root != null && tab != null && (n = tab.length) > 0) {
//                // 根据根节点的Hash值 和 HashMap的元素数组长度  取得根节点在数组中的位置
//                int index = (n - 1) & root.hash;
//                // 首先取得该位置上的第一个节点对象
//                TreeNode<K, V> first = (TreeNode<K, V>) tab[index];
//                // 如果该节点对象 与 根节点对象 不同
//                if (root != first) {
//                    Node<K, V> rn; // 定义根节点的后一个节点
//                    tab[index] = root; // 把元素数组index位置的元素替换为根节点对象
//                    TreeNode<K, V> rp = root.prev; // 获取根节点对象的前一个节点
//                    if ((rn = root.next) != null) // 如果后节点不为空
//                        ((TreeNode<K, V>) rn).prev = rp; // root后节点的前节点  指向到 root的前节点，相当于把root从链表中摘除
//                    if (rp != null) // 如果root的前节点不为空
//                        rp.next = rn; // root前节点的后节点 指向到 root的后节点
//                    if (first != null) // 如果数组该位置上原来的元素不为空
//                        first.prev = root; // 这个原有的元素的 前节点 指向到 root，相当于root目前位于链表的首位
//                    root.next = first; // 原来的第一个节点现在作为root的下一个节点，变成了第二个节点
//                    root.prev = null; // 首节点没有前节点
//                }
//
//                /*
//                 * 这一步是防御性的编程
//                 * 校验TreeNode对象是否满足红黑树和双链表的特性
//                 * 如果这个方法校验不通过：可能是因为用户编程失误，破坏了结构（例如：并发场景下）；也可能是TreeNode的实现有问题（这个是理论上的以防万一）；
//                 */
//                assert checkInvariants(root);
//            }
//        }
//
//        /**
//         * Finds the node starting at root p with the given hash and key.
//         * The kc argument caches comparableClassFor(key) upon first use
//         * comparing keys.
//         */
//        final TreeNode<K, V> find(int h, Object k, Class<?> kc) {
//            TreeNode<K, V> p = this;
//            do {
//                int ph, dir;
//                K pk;
//                TreeNode<K, V> pl = p.left, pr = p.right, q;
//                if ((ph = p.hash) > h)
//                    p = pl;
//                else if (ph < h)
//                    p = pr;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if (pl == null)
//                    p = pr;
//                else if (pr == null)
//                    p = pl;
//                else if ((kc != null ||
//                        (kc = comparableClassFor(k)) != null) &&
//                        (dir = compareComparables(kc, k, pk)) != 0)
//                    p = (dir < 0) ? pl : pr;
//                else if ((q = pr.find(h, k, kc)) != null)
//                    return q;
//                else
//                    p = pl;
//            } while (p != null);
//            return null;
//        }
//
//        /**
//         * Calls find for root node.
//         */
//        final TreeNode<K, V> getTreeNode(int h, Object k) {
//            return ((parent != null) ? root() : this).find(h, k, null);
//        }
//
//        /**
//         * Tie-breaking utility for ordering insertions when equal
//         * hashCodes and non-comparable. We don't require a total
//         * order, just a consistent insertion rule to maintain
//         * equivalence across rebalancings. Tie-breaking further than
//         * necessary simplifies testing a bit.
//         */
//        static int tieBreakOrder(Object a, Object b) {
//            int d;
//            if (a == null || b == null ||
//                    (d = a.getClass().getName().
//                            compareTo(b.getClass().getName())) == 0)
//                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
//                        -1 : 1);
//            return d;
//        }
//
//        /**
//         * Forms tree of the nodes linked from this node.
//         */
//        final void treeify(Node<K, V>[] tab) {
//            TreeNode<K, V> root = null;
//            for (TreeNode<K, V> x = this, next; x != null; x = next) {
//                next = (TreeNode<K, V>) x.next;
//                x.left = x.right = null;
//                if (root == null) {
//                    x.parent = null;
//                    x.red = false;
//                    root = x;
//                } else {
//                    K k = x.key;
//                    int h = x.hash;
//                    Class<?> kc = null;
//                    for (TreeNode<K, V> p = root; ; ) {
//                        int dir, ph;
//                        K pk = p.key;
//                        if ((ph = p.hash) > h)
//                            dir = -1;
//                        else if (ph < h)
//                            dir = 1;
//                        else if ((kc == null &&
//                                (kc = comparableClassFor(k)) == null) ||
//                                (dir = compareComparables(kc, k, pk)) == 0)
//                            dir = tieBreakOrder(k, pk);
//
//                        TreeNode<K, V> xp = p;
//                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                            x.parent = xp;
//                            if (dir <= 0)
//                                xp.left = x;
//                            else
//                                xp.right = x;
//                            root = balanceInsertion(root, x);
//                            break;
//                        }
//                    }
//                }
//            }
//            moveRootToFront(tab, root);
//        }
//
//        /**
//         * Returns a list of non-TreeNodes replacing those linked from
//         * this node.
//         */
//        final Node<K, V> untreeify(HashMap<K, V> map) {
//            Node<K, V> hd = null, tl = null;
//            for (Node<K, V> q = this; q != null; q = q.next) {
//                Node<K, V> p = map.replacementNode(q, null);
//                if (tl == null)
//                    hd = p;
//                else
//                    tl.next = p;
//                tl = p;
//            }
//            return hd;
//        }
//
//        /**
//         * Tree version of putVal.
//         */
//        final TreeNode<K, V> putTreeVal(HashMap<K, V> map, Node<K, V>[] tab,
//                                        int h, K k, V v) {
//            Class<?> kc = null;
//            boolean searched = false;
//            TreeNode<K, V> root = (parent != null) ? root() : this;
//            for (TreeNode<K, V> p = root; ; ) {
//                int dir, ph;
//                K pk;
//                if ((ph = p.hash) > h)
//                    dir = -1;
//                else if (ph < h)
//                    dir = 1;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if ((kc == null &&
//                        (kc = comparableClassFor(k)) == null) ||
//                        (dir = compareComparables(kc, k, pk)) == 0) {
//                    if (!searched) {
//                        TreeNode<K, V> q, ch;
//                        searched = true;
//                        if (((ch = p.left) != null &&
//                                (q = ch.find(h, k, kc)) != null) ||
//                                ((ch = p.right) != null &&
//                                        (q = ch.find(h, k, kc)) != null))
//                            return q;
//                    }
//                    dir = tieBreakOrder(k, pk);
//                }
//
//                TreeNode<K, V> xp = p;
//                if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                    Node<K, V> xpn = xp.next;
//                    TreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
//                    if (dir <= 0)
//                        xp.left = x;
//                    else
//                        xp.right = x;
//                    xp.next = x;
//                    x.parent = x.prev = xp;
//                    if (xpn != null)
//                        ((TreeNode<K, V>) xpn).prev = x;
//                    moveRootToFront(tab, balanceInsertion(root, x));
//                    return null;
//                }
//            }
//        }
//
//        /**
//         * Removes the given node, that must be present before this call.
//         * This is messier than typical red-black deletion code because we
//         * cannot swap the contents of an interior node with a leaf
//         * successor that is pinned by "next" pointers that are accessible
//         * independently during traversal. So instead we swap the tree
//         * linkages. If the current tree appears to have too few nodes,
//         * the bin is converted back to a plain bin. (The test triggers
//         * somewhere between 2 and 6 nodes, depending on tree structure).
//         */
//        final void removeTreeNode(HashMap<K, V> map, Node<K, V>[] tab,
//                                  boolean movable) {
//            int n;
//            if (tab == null || (n = tab.length) == 0)
//                return;
//            int index = (n - 1) & hash;
//            TreeNode<K, V> first = (TreeNode<K, V>) tab[index], root = first, rl;
//            TreeNode<K, V> succ = (TreeNode<K, V>) next, pred = prev;
//            if (pred == null)
//                tab[index] = first = succ;
//            else
//                pred.next = succ;
//            if (succ != null)
//                succ.prev = pred;
//            if (first == null)
//                return;
//            if (root.parent != null)
//                root = root.root();
//            if (root == null
//                    || (movable
//                    && (root.right == null
//                    || (rl = root.left) == null
//                    || rl.left == null))) {
//                tab[index] = first.untreeify(map);  // too small
//                return;
//            }
//            TreeNode<K, V> p = this, pl = left, pr = right, replacement;
//            if (pl != null && pr != null) {
//                TreeNode<K, V> s = pr, sl;
//                while ((sl = s.left) != null) // find successor
//                    s = sl;
//                boolean c = s.red;
//                s.red = p.red;
//                p.red = c; // swap colors
//                TreeNode<K, V> sr = s.right;
//                TreeNode<K, V> pp = p.parent;
//                if (s == pr) { // p was s's direct parent
//                    p.parent = s;
//                    s.right = p;
//                } else {
//                    TreeNode<K, V> sp = s.parent;
//                    if ((p.parent = sp) != null) {
//                        if (s == sp.left)
//                            sp.left = p;
//                        else
//                            sp.right = p;
//                    }
//                    if ((s.right = pr) != null)
//                        pr.parent = s;
//                }
//                p.left = null;
//                if ((p.right = sr) != null)
//                    sr.parent = p;
//                if ((s.left = pl) != null)
//                    pl.parent = s;
//                if ((s.parent = pp) == null)
//                    root = s;
//                else if (p == pp.left)
//                    pp.left = s;
//                else
//                    pp.right = s;
//                if (sr != null)
//                    replacement = sr;
//                else
//                    replacement = p;
//            } else if (pl != null)
//                replacement = pl;
//            else if (pr != null)
//                replacement = pr;
//            else
//                replacement = p;
//            if (replacement != p) {
//                TreeNode<K, V> pp = replacement.parent = p.parent;
//                if (pp == null)
//                    root = replacement;
//                else if (p == pp.left)
//                    pp.left = replacement;
//                else
//                    pp.right = replacement;
//                p.left = p.right = p.parent = null;
//            }
//
//            TreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);
//
//            if (replacement == p) {  // detach
//                TreeNode<K, V> pp = p.parent;
//                p.parent = null;
//                if (pp != null) {
//                    if (p == pp.left)
//                        pp.left = null;
//                    else if (p == pp.right)
//                        pp.right = null;
//                }
//            }
//            if (movable)
//                moveRootToFront(tab, r);
//        }
//
//        /**
//         * Splits nodes in a tree bin into lower and upper tree bins,
//         * or untreeifies if now too small. Called only from resize;
//         * see above discussion about split bits and indices.
//         *
//         * @param map   the map
//         * @param tab   the table for recording bin heads
//         * @param index the index of the table being split
//         * @param bit   the bit of hash to split on
//         */
//        final void split(HashMap<K, V> map, Node<K, V>[] tab, int index, int bit) {
//            TreeNode<K, V> b = this;
//            // Relink into lo and hi lists, preserving order
//            TreeNode<K, V> loHead = null, loTail = null;
//            TreeNode<K, V> hiHead = null, hiTail = null;
//            int lc = 0, hc = 0;
//            for (TreeNode<K, V> e = b, next; e != null; e = next) {
//                next = (TreeNode<K, V>) e.next;
//                e.next = null;
//                if ((e.hash & bit) == 0) {
//                    if ((e.prev = loTail) == null)
//                        loHead = e;
//                    else
//                        loTail.next = e;
//                    loTail = e;
//                    ++lc;
//                } else {
//                    if ((e.prev = hiTail) == null)
//                        hiHead = e;
//                    else
//                        hiTail.next = e;
//                    hiTail = e;
//                    ++hc;
//                }
//            }
//
//            if (loHead != null) {
//                if (lc <= UNTREEIFY_THRESHOLD)
//                    tab[index] = loHead.untreeify(map);
//                else {
//                    tab[index] = loHead;
//                    if (hiHead != null) // (else is already treeified)
//                        loHead.treeify(tab);
//                }
//            }
//            if (hiHead != null) {
//                if (hc <= UNTREEIFY_THRESHOLD)
//                    tab[index + bit] = hiHead.untreeify(map);
//                else {
//                    tab[index + bit] = hiHead;
//                    if (loHead != null)
//                        hiHead.treeify(tab);
//                }
//            }
//        }
//
//        /* ------------------------------------------------------------ */
//        // Red-black tree methods, all adapted from CLR
//
//        static <K, V> TreeNode<K, V> rotateLeft(TreeNode<K, V> root,
//                                                TreeNode<K, V> p) {
//            TreeNode<K, V> r, pp, rl;
//            if (p != null && (r = p.right) != null) {
//                if ((rl = p.right = r.left) != null)
//                    rl.parent = p;
//                if ((pp = r.parent = p.parent) == null)
//                    (root = r).red = false;
//                else if (pp.left == p)
//                    pp.left = r;
//                else
//                    pp.right = r;
//                r.left = p;
//                p.parent = r;
//            }
//            return root;
//        }
//
//        static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root,
//                                                 TreeNode<K, V> p) {
//            TreeNode<K, V> l, pp, lr;
//            if (p != null && (l = p.left) != null) {
//                if ((lr = p.left = l.right) != null)
//                    lr.parent = p;
//                if ((pp = l.parent = p.parent) == null)
//                    (root = l).red = false;
//                else if (pp.right == p)
//                    pp.right = l;
//                else
//                    pp.left = l;
//                l.right = p;
//                p.parent = l;
//            }
//            return root;
//        }
//
//        static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root,
//                                                      TreeNode<K, V> x) {
//            x.red = true;
//            for (TreeNode<K, V> xp, xpp, xppl, xppr; ; ) {
//                if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                } else if (!xp.red || (xpp = xp.parent) == null)
//                    return root;
//                if (xp == (xppl = xpp.left)) {
//                    if ((xppr = xpp.right) != null && xppr.red) {
//                        xppr.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    } else {
//                        if (x == xp.right) {
//                            root = rotateLeft(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateRight(root, xpp);
//                            }
//                        }
//                    }
//                } else {
//                    if (xppl != null && xppl.red) {
//                        xppl.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    } else {
//                        if (x == xp.left) {
//                            root = rotateRight(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateLeft(root, xpp);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root,
//                                                     TreeNode<K, V> x) {
//            for (TreeNode<K, V> xp, xpl, xpr; ; ) {
//                if (x == null || x == root)
//                    return root;
//                else if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                } else if (x.red) {
//                    x.red = false;
//                    return root;
//                } else if ((xpl = xp.left) == x) {
//                    if ((xpr = xp.right) != null && xpr.red) {
//                        xpr.red = false;
//                        xp.red = true;
//                        root = rotateLeft(root, xp);
//                        xpr = (xp = x.parent) == null ? null : xp.right;
//                    }
//                    if (xpr == null)
//                        x = xp;
//                    else {
//                        TreeNode<K, V> sl = xpr.left, sr = xpr.right;
//                        if ((sr == null || !sr.red) &&
//                                (sl == null || !sl.red)) {
//                            xpr.red = true;
//                            x = xp;
//                        } else {
//                            if (sr == null || !sr.red) {
//                                if (sl != null)
//                                    sl.red = false;
//                                xpr.red = true;
//                                root = rotateRight(root, xpr);
//                                xpr = (xp = x.parent) == null ?
//                                        null : xp.right;
//                            }
//                            if (xpr != null) {
//                                xpr.red = (xp == null) ? false : xp.red;
//                                if ((sr = xpr.right) != null)
//                                    sr.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateLeft(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                } else { // symmetric
//                    if (xpl != null && xpl.red) {
//                        xpl.red = false;
//                        xp.red = true;
//                        root = rotateRight(root, xp);
//                        xpl = (xp = x.parent) == null ? null : xp.left;
//                    }
//                    if (xpl == null)
//                        x = xp;
//                    else {
//                        TreeNode<K, V> sl = xpl.left, sr = xpl.right;
//                        if ((sl == null || !sl.red) &&
//                                (sr == null || !sr.red)) {
//                            xpl.red = true;
//                            x = xp;
//                        } else {
//                            if (sl == null || !sl.red) {
//                                if (sr != null)
//                                    sr.red = false;
//                                xpl.red = true;
//                                root = rotateLeft(root, xpl);
//                                xpl = (xp = x.parent) == null ?
//                                        null : xp.left;
//                            }
//                            if (xpl != null) {
//                                xpl.red = (xp == null) ? false : xp.red;
//                                if ((sl = xpl.left) != null)
//                                    sl.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateRight(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * Recursive invariant check
//         */
//        static <K, V> boolean checkInvariants(TreeNode<K, V> t) {
//            TreeNode<K, V> tp = t.parent, tl = t.left, tr = t.right,
//                    tb = t.prev, tn = (TreeNode<K, V>) t.next;
//            if (tb != null && tb.next != t)
//                return false;
//            if (tn != null && tn.prev != t)
//                return false;
//            if (tp != null && t != tp.left && t != tp.right)
//                return false;
//            if (tl != null && (tl.parent != t || tl.hash > t.hash))
//                return false;
//            if (tr != null && (tr.parent != t || tr.hash < t.hash))
//                return false;
//            if (t.red && tl != null && tl.red && tr != null && tr.red)
//                return false;
//            if (tl != null && !checkInvariants(tl))
//                return false;
//            if (tr != null && !checkInvariants(tr))
//                return false;
//            return true;
//        }
//    }
//
//
//    public HashMap(float loadFactor) {
//        this.loadFactor = loadFactor;
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;
//    }
//}
