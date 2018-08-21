package test;

/**
 * @Class: TestOfPhatomReference
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/20
 */

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 测试 虚引用
 * 虚引用主要用于检测对象是否已从内存中回收。
 *
 * jvm有四种引用: strong soft weak phantom(其实还有一种FinalReference，这个由jvm自己使用，外部无法调用到），
 * 主要的区别体现在gc上的处理，如下：
 * Strong类型，也就是正常使用的类型，不需要显示定义，只要没有任何引用就可以回收
 * SoftReference类型，如果一个对象只剩下一个soft引用，在jvm内存不足的时候会将这个对象进行回收
 * WeakReference类型，如果对象只剩下一个weak引用，那gc的时候就会回收。和SoftReference都可以用来实现cache
 * PhantomReference类型，这个比较特殊，可以用来实现类似Object.finalize功能，下面单独解释。
 *
 * soft类型由于内存还充足，不会被回收；weak类型在gc的时候就回收；
 * phantom总是返回null。weakString没被回收是引用常量池持有对"abc"的引用。
 *
 * 其意义在于说明一个对象已经进入finalization阶段，可以被gc回收，用来实现比finalization机制更灵活的回收操作。
 */
public class TestOfPhatomReference {
    public static void main(String[] args) {
        Object object = new Object();
        PhantomReference<Object> phantomReference =
                new PhantomReference<Object>(object, new ReferenceQueue<Object>());
        SoftReference<Object> objectSoftReference = new SoftReference<Object>(object);
//        System.gc();
        System.out.println("objectSoftReference object: " + objectSoftReference.get());
        System.out.println("phantomReference object: " + phantomReference.get());
    }
}
