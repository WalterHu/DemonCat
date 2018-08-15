package test;

/**
 * @Class: TestOfArray
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.Arrays;

/**
 * 测试Array数组的拷贝
 */
public class TestOfArray {
    public static void main(String[] args) {
        String[] strings1 = new String[5];
        for (int i = 0; i < strings1.length; i ++) {
            strings1[i] = i + "";
        }
        int length = strings1.length;
        // 执行一般拷贝
        final long start = System.currentTimeMillis();
        String[] strings = new String[length];
        for (int i = 0; i < length; i ++) { // 尽量不要使用size或者length调用作为i的大小判断条件
            strings[i] = strings1[i];
        }
        final long end = System.currentTimeMillis();
        System.out.println("Common copy cost: " + (end - start)); // cost 0
        // 执行Arrays拷贝，其内部实现是使用的System类的arrayCopy方法
        final long start1 = System.currentTimeMillis();
        String[] strings2 = Arrays.copyOf(strings1, length);
        final long start2 = System.currentTimeMillis();
        System.out.println("Arrays.copy cost: " + (start2 - start1)); // cost 0
        // 执行System类的arrayCopy方法
        final long start3 = System.currentTimeMillis();
        String[] strings3 = new String[length];
        System.arraycopy(strings1, 0, strings3, 0, length);
        final long start4 = System.currentTimeMillis();
        System.out.println("System.arrayCopy cost: " + (start4 - start3)); // cost 0
        // Arrays填充数组为相同元素
        String[] fillStr = new String[5];
        Arrays.fill(fillStr, "abc");
        for (String str : fillStr) {
            System.out.println("fill str: " + str == null ? "null" : str);
        }
    }
}
