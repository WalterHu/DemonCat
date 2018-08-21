package test;

/**
 * @Class: TestOfReference
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/21
 */

import java.lang.ref.WeakReference;

/**
 * Test of Reference.
 *
 */
public class TestOfReference {
    public static void main(String[] args) {
        // 由于pending是由JVM来赋值的，当Reference内部的referent对象的可达状态发生改变时，
        // JVM会将Reference对象放入到pending链表中。因此，
        // 在例子中的代码o = null这一句，它使得o对象满足垃圾回收的条件，并且在后边显示调用了System.gc(),
        // 垃圾收集进行的时候会标记WeakReference的referent的对象o为不可达(使得wr.get()==null),
        // 并且通过赋值给pending，触发ReferenceHandler线程来处理pending.ReferenceHandler线程要做的是将pending对象enqueue。
        // 但在这个程序中我们从构造函数传入的为null,即实际使用的是ReferenceQueue.NULL,
        // ReferenceHandler线程判断queue如果为ReferenceQueue.NULL则不进行enqueue，如果不是，则进行enqueue操作。
        // ReferenceQueue.NULL相当于我们提供了一个空的Queue去监听垃圾回收器给我们的反馈，并且对这种反馈不做任何处理。
        Object o = new Object();
        WeakReference<Object> reference = new WeakReference<Object>(o);
        System.out.println("reference active: " + reference.get());
        o = null;
        System.gc();
        System.out.println("reference gc: " + reference.get());

        // 当直接访问一个类或者接口的静态变量时，只有真正声明该变量的类或者接口才会被初始化
        System.out.println("a: " + B.a);
    }
}

class A {
    // 类进入初始化阶段的时候，JVM负责对类进行初始化，也就是对静态属性进行初始化。
    // 对静态属性指定初始值的方式有两种:
    // a.声明静态属性时指定初始值
    // b.使用静态初始化快为静态属性指定初始值
    static int a = 6;
    static {
        a = 9;
        System.out.println("A a: " + a);
    }
}

class B extends A {
    static {
        System.out.println("B a: " + a);
    }
}
