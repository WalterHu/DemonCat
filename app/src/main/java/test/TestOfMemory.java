package test;

/**
 * @Class: TestOfMemory
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/20
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 测试JVM中相关数据区的内存溢出
 */
public class TestOfMemory {
    public static void main(String[] args) {
//        TestOfOOM.test(); // 堆内存溢出
//        new TestOfStack().plus(); // 栈内存溢出
        TestOfStaticConstants.test(); // 常量池溢出
    }
}

/**
 * 堆内存溢出（对象无限创建）
 * @VM args:-verbose:gc -Xms20M -Xmx20M -XX:+PrintGCDetails
 *
 * Java 堆内存的OutOfMemoryError异常是实际应用中最常见的内存溢出异常情况。出现Java 堆内
 * 存溢出时，异常堆栈信息“java.lang.OutOfMemoryError”会跟着进一步提示“Java heap
 * space”。
 */
class TestOfOOM {
    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     at java.util.Arrays.copyOf(Arrays.java:3210)
     at java.util.Arrays.copyOf(Arrays.java:3181)
     at java.util.ArrayList.grow(ArrayList.java:261)
     at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:235)
     at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:227)
     at java.util.ArrayList.add(ArrayList.java:458)
     at test.TestOfOOM.test(TestOfMemory.java:29)
     at test.TestOfMemory.main(TestOfMemory.java:18)
     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     at java.lang.reflect.Method.invoke(Method.java:498)
     at com.intellij.rt.execution.application.AppMainV2.main(AppMainV2.java:131)
     */
    public static void test() {
        List<TestOfOOM> oomList = new ArrayList<>();
        for (;;) {
            oomList.add(new TestOfOOM());
        }
    }
}

/**
 * java栈溢出
 * @Described：栈层级不足探究 StackOverFlow
 * @VM args:-Xss128k
 */
class TestOfStack {
    private int i = 0;

    public void plus() {
        i ++;
        plus();
    }
}

/**
 * 常量池溢出
 * @VM args: -XX:PermSize=10M -XX:MaxPermSize=10M
 */
class TestOfStaticConstants {
    public static void test() {
        List<String> strings = new ArrayList<>();
        int i = 0;
        for (;;) {
            strings.add(String.valueOf(i).intern());
        }
    }
}
