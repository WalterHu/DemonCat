package test;

/**
 * @Class: TestOfJVM
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/20
 */

/**
 * 测试虚拟机栈的大小
 * i:18603
 *
 * Android程序栈内存大小仅为8M，不受操作系统版本及其他因素影响
 *
 * @see JVM-README
 */
public class TestOfJVM {
    private int i = 0; // 基本数据类型

    public static void main(String[] args) {
        TestOfJVM jvm = new TestOfJVM();
        try {
            jvm.recursion();
        } catch (Throwable t) {
            System.out.println("i : " + jvm.i);
            t.printStackTrace(); // java.lang.StackOverflowError
        }
    }

    private void recursion() {
        i ++;
        recursion();
    }
}
