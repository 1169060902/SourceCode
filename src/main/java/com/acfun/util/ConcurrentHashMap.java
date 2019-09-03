//package com.acfun.util;
//
//import javax.swing.text.Segment;
//import java.io.ObjectStreamField;
//import java.io.Serializable;
//import java.util.AbstractMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentMap;
//import java.util.function.BiConsumer;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
///**
// * @param <K> key
// * @param <V> value
// *            AbstractMap  实现了大部分方法
// *            ConcurrentMap 实现了大部分map方法
// *            Serializable 可序列化
// */
//public class ConcurrentHashMap<K, V> extends AbstractMap<K, V>
//        implements ConcurrentMap<K, V>, Serializable {
//    /**
//     * 序列号ID
//     */
//    private static final long serialVersionUID = 7249069246763182397L;
//    /**
//     * 表的最大容量
//     * The largest possible table capacity
//     */
//    private static final int MAXIMUM_CAPACITY = 1 << 30;
//    /**
//     *
//     */
//
//    /**
//     * 默认表的大小
//     * The default initial table capacity
//     */
//    private static final int DEFAULT_CAPACITY = 16;
//    /**
//     * 最大数组大小
//     * The largest possible (non-power of two) array size
//     */
//    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//    /**
//     * 默认并发数
//     */
//    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
//    /**
//     * 装载因
//     */
//    private static final float LOAD_FACTOR = 0.75f;
//
//    /**
//     * 转化为红黑树的阈值
//     */
//    static final int TREEIFY_THRESHOLD = 8;
//
//    /**
//     * 由红黑树转化为链表的阈值
//     */
//    static final int UNTREEIFY_THRESHOLD = 6;
//    /**
//     * 转化为红黑树的表的最小容量
//     */
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//
//    /**
//     * 每次进行转移的最小值
//     */
//    private static final int MIN_TRANSFER_STRIDE = 16;
//
//    /**
//     * 生成sizeCtl所使用的bit位数
//     */
//    private static int RESIZE_STAMP_BITS = 16;
//
//
//    /**
//     * 进行扩容所允许的最大线程数
//     */
//    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;
//    /**
//     * 记录sizeCtl中的大小所需要进行的偏移位数
//     */
//    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;
//    // 一系列的标识
//    static final int MOVED = -1; // hash for forwarding nodes
//    static final int TREEBIN = -2; // hash for roots of trees
//    static final int RESERVED = -3; // hash for transient reservations
//    static final int HASH_BITS = 0x7fffffff; // usable bits of normal node hash
//
//    /**
//     * 获取可用的CPU个数
//     * Number of CPUS, to place bounds on some sizings
//     */
//    static final int NCPU = Runtime.getRuntime().availableProcessors();
//
//    /**
//     * The array of bins. Lazily initialized upon first insertion.
//     * Size is always a power of two. Accessed directly by iterators.
//     * 表
//     */
//    transient volatile Node<K,V>[] table;
//
//    /**
//     * The next table to use; non-null only while resizing.
//     * 下一个表
//     */
//    private transient volatile Node<K,V>[] nextTable;
//
//    /**
//     * Base counter value, used mainly when there is no contention,
//     * but also as a fallback during table initialization
//     * races. Updated via CAS.
//     * 基本计数
//     */
//    private transient volatile long baseCount;
//
//    /**
//     * Table initialization and resizing control.  When negative, the
//     * table is being initialized or resized: -1 for initialization,
//     * else -(1 + the number of active resizing threads).  Otherwise,
//     * when table is null, holds the initial table size to use upon
//     * creation, or 0 for default. After initialization, holds the
//     * next element count value upon which to resize the table.
//     *  对表初始化和扩容控制
//     */
//    private transient volatile int sizeCtl;
//
//    /**
//     * The next table index (plus one) to split while resizing.
//     * 扩容下另一个表的索引
//     */
//    private transient volatile int transferIndex;
//
//    /**
//     * Spinlock (locked via CAS) used when resizing and/or creating CounterCells.
//     *  旋转锁
//     */
//    private transient volatile int cellsBusy;
//
//    /**
//     * Table of counter cells. When non-null, size is a power of 2.
//     * counterCell表
//     */
//    private transient volatile CounterCell[] counterCells;
//
//    // views
////    private transient KeySetView<K,V> keySet;
////    private transient ValuesView<K,V> values;
////    private transient EntrySetView<K,V> entrySet;
//
//    @sun.misc.Contended static final class CounterCell {
//        volatile long value;
//        CounterCell(long x) { value = x; }
//    }
//
//    /**
//     * 进行序列化的属性
//     * For serialization compatibility.
//     */
//    private static final ObjectStreamField[] serialPersistentFields = {
//            new ObjectStreamField("segments", Segment[].class),
//            new ObjectStreamField("segmentMask", Integer.TYPE),
//            new ObjectStreamField("segmentShift", Integer.TYPE)
//    };
//
//    static class Node<K,V> implements Map.Entry<K,V> {
//        final int hash;
//        final K key;
//        volatile V val;
//        volatile Node<K,V> next;
//
//        Node(int hash, K key, V val, Node<K,V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.val = val;
//            this.next = next;
//        }
//
//        public final K getKey()       { return key; }
//        public final V getValue()     { return val; }
//        public final int hashCode()   { return key.hashCode() ^ val.hashCode(); }
//        public final String toString(){ return key + "=" + val; }
//        public final V setValue(V value) {
//            throw new UnsupportedOperationException();
//        }
//
//        public final boolean equals(Object o) {
//            Object k, v, u; Map.Entry<?,?> e;
//            return ((o instanceof Map.Entry) &&
//                    (k = (e = (Map.Entry<?,?>)o).getKey()) != null &&
//                    (v = e.getValue()) != null &&
//                    (k == key || k.equals(key)) &&
//                    (v == (u = val) || v.equals(u)));
//        }
//
//        /**
//         * Virtualized support for map.get(); overridden in subclasses.
//         */
//        Node<K,V> find(int h, Object k) {
//            Node<K,V> e = this;
//            if (k != null) {
//                do {
//                    K ek;
//                    if (e.hash == h &&
//                            ((ek = e.key) == k || (ek != null && k.equals(ek))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//            return null;
//        }
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;
//    }
//
//    @Override
//    public V getOrDefault(Object key, V defaultValue) {
//        return null;
//    }
//
//    @Override
//    public void forEach(BiConsumer<? super K, ? super V> action) {
//
//    }
//
//    @Override
//    public boolean remove(Object key, Object value) {
//        return false;
//    }
//
//    @Override
//    public boolean replace(K key, V oldValue, V newValue) {
//        return false;
//    }
//
//    @Override
//    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//
//    }
//
//    @Override
//    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
//        return null;
//    }
//
//    @Override
//    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        return null;
//    }
//
//    @Override
//    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        return null;
//    }
//
//    @Override
//    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
//        return null;
//    }
//}
