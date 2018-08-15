package test;

/**
 * @Class: TestOfSet
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Collection of Set.
 * Set内元素不能够重复. List内元素可以重复.
 */

/**
 * TreeMap和TreeSet的异同：

 相同点：

 TreeMap和TreeSet都是有序的集合，也就是说他们存储的值都是排好序的。
 TreeMap和TreeSet都是非同步集合，因此他们不能在多线程之间共享，不过可以使用方法Collections.synchroinzedMap()来实现同步
 运行速度都要比Hash集合慢，他们内部对元素的操作时间复杂度为O(logN)，而HashMap/HashSet则为O(1)。
 不同点：

 最主要的区别就是TreeSet和TreeMap非别实现Set和Map接口
 TreeSet只存储一个对象，而TreeMap存储两个对象Key和Value（仅仅key对象有序）
 TreeSet中不能有重复对象，而TreeMap中可以存在
 TreeSet的是NavigableSet的实现类，NavigableSet继承了SortedSet接口，SortedSet是Set的子接口；
 */
public class TestOfSet {
    public static void main(String[] args) {
        Set<String> hasSet = new HashSet<>();
        boolean result = hasSet.add("123");
        System.out.println("result: " + result);
        result = hasSet.add("123"); // 重复添加元素123
        System.out.println("result: " + result);
        // HashSet内部使用HashMap实现.
        // 其存放的对象使用HashMap的Key来存储.
        // value则直接使用了一个静态常量Object对象作为占位.
        System.out.println(hasSet.size());
        // TreeSet底层使用红黑树来存储集合元素。
        // 存储对象必须实现Comparable接口。
        // TreeSet按照元素值排序，比HashSet慢。
        Set<String> treeSet = new TreeSet<>();
    }
}
