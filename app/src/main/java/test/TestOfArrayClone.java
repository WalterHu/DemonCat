package test;

/**
 * @Class: TestOfArrayClone
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 测试数组拷贝
 */
public class TestOfArrayClone {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            strings.add(i + "");
        }
        Object[] strings1 = strings.toArray(); // 内部使用的Arrays.copyOf方法
        String[] strings2 = new String[3];
        strings2 = strings.toArray(strings2);
        String[] strings3 = new String[6];
        strings3 = strings.toArray(strings3);
        System.out.println(strings1.length); // 5
        System.out.println(strings2.length); // 5
        System.out.println(strings3.length); // 6
        System.out.println(
                strings3[strings3.length - 1] == null ?
                        "null" : strings3[strings3.length - 1]); // 'null'
        // 如果数组长度比集合长度长，则会将多余的数组元素置空
        // 如果数组长度比集合长度短，则会创建集合长度的数组并返回
    }
}
