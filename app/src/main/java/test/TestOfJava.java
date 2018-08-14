package test;

/**
 * @Class: TestOfJava
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/13
 */
public class TestOfJava {
    public static void main(String[] args) {
        Integer a = 8;
        Integer b = 8;
        Integer c = new Integer(8);
        System.out.println("a == b ? " + (a == b));
        System.out.println("a == c ? " + (a == c));
        Integer d = 200;
        Integer e = 200;
        System.out.println("d == e ? " + (d == e));
        // a 和 b 相等，因为Byte/Short/Integer/Long类型在系统初始化时
        // 创建了-127～128的对象缓存，取值在该范围会直接返回缓存对象，内存地址相同。
        // a 和 c 不等，因为c引用了新的Integer对象，内存地址不同。
        // d 和 e 不等，因为缓存值范围是-127～128，不是200，导致引用封装创建了新的应用。
        short s1 = 1;
        // s1 = s1 + 1; // 因为做加运算的时候，1不能够被降级转成short，会编译错误
        s1 = (short) (s1 + 1); // 强制转化成short类型
        s1 += 1; // 该写法与上一行写发类似，先做int加，然后强转成short
        char c1 = 'A';
        int i1 = c1;
        System.out.println("c1: " + c1 + ", i1: " + i1);
    }
}
