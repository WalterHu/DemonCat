package test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: TestOfString
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/14
 */
public class TestOfString {
    public static void main(String args[]) {
        // String of intern() and equals()
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        String s4 = "a" + "b";
        String s5 = "a" + s2;
        String s6 = new String("a" + "b");
        System.out.println(s3 == s6); // false, 因为s3在String池中，但是s6是new String的对象，不是相同对象
        System.out.println(s3.equals(s6)); // true, s3与s6的内容相同，所以是true
        System.out.println(s6.intern() == s3); // true, s3的对象实际是String池中的对象，intern方法返回的是池中对象，所以相同
        System.out.println(s3 == s4); // true, JVM虚拟机会优化两个静态字符串的拼接，所以两者相同
        System.out.println(s3 == s5); // false, 动态字符串拼接会在池中创建新的对象
        System.out.println(s3.intern() == s5); // false, 动态字符串拼接会在池中创建新的对象, s3的静态字符串对象与s5不同
        System.out.println(s5.intern() == s3); // true, intern会先从池中寻找当前内容对象，此时该池中对象就是s3所指字符串
        System.out.println(s5.intern() == s6); // false, intern会先从池中寻找当前内容对象，此时池中对象是s3对应静态字符串，s6是另外的对象
        System.out.println(s6.intern() == s5); // false, intern会先从池中寻找当前内容对象，此时池中对象是s3对应静态字符串，s6是另外的对象

        // String of trim()
        // trim方法可以去掉String首尾的空格，实际是去掉首尾所有小于或者等于32的char，32的char刚好是空格
        char blank = 32;
        System.out.println("black:" + blank + "!");
        String stringBlk = blank + "aaabbbccc";
        System.out.println("stringBlk:" + stringBlk + "!");
        System.out.println("stringBlk.trim:" + stringBlk.trim() + "!");

        // Substring
        // String strSub = "abcdefghijklmnopqrstuvwxyz";
        // strSub.substring(1, 10); // private native String fastSubstring(int start, int length);
        // 下述代码在JDK6上会引发OOM内存溢出的问题。subString在JDK6中存放的是索引和字符串引用，因此循环
        // 结束的时候new出来的HugeStr对象并不能够释放，并且每个对象中都保留了原始String，导致内存溢出。
        // 但是如果采用new String来存放subString的方式，则会释放掉之前的大额数据，因此不会内存溢出。
        // 该问题已经在JDK7中修复。
        List<String> strs = new ArrayList<>();
        for (int i = 0; i < 1000; i ++) {
            strs.add(new HugeStr().getSubString(1, 10));
        }
        // StringBuffer是线程安全的，StringBuilder是非线程安全的
        // 方法内部使用StringBuilder会比StringBuffer效率快10%-15%
    }

    static class HugeStr {
        private String str = new String(new char[10000000]);

        // 因为String源码中的subString方法是new了一个String对象，
        // 里面保存了原始的String的char[]和偏移量。这样节省了内存空间，
        // 因为new出来的String服用了原String的内存空间，
        // 但是从一个巨大的String中截取一小部分String，这样会导致大量的冗余。
        // 因此，当从巨大的String中截取一小部分String，建议new一个String来保存。
        public String getSubString(int begin, int end) {
            return str.substring(begin, end); // 不建议使用
            // return new String(str.substring(begin, end)); // 建议使用
        }
    }
}
