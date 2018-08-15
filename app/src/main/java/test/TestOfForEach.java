package test;

/**
 * @Class: TestOfForEach
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试foreach
 */
public class TestOfForEach {
    public static void main(String[] args) {
        List<String> list =
                Arrays.asList(new String[] {"1", "2", "3", "4", "5"});
        list = new ArrayList<>(list);
        for (String element : list) {
            if (element.equals("3")) {
                // 使用foreach迭代器时，不能够在迭代时直接删除元素。
                // 会报出java.util.ConcurrentModificationException异常。
                list.remove(element);
            }
        }
    }
}
