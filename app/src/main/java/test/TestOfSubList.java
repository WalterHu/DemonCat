package test;

/**
 * @Class: TestOfSubList
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 测试List截取动作subList。
 * 用subList返回的是源数据的直接引用。
 */
public class TestOfSubList {
    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>(Arrays.asList(new String[] {"1", "2", "3", "4", "5"}));
        System.out.println("list before modification: " + list1.hashCode());
        List<String> list2 = list1.subList(0, 3);
        System.out.println("list after modification: " + list2.hashCode());
        // 修改源List的第一个元素
        list1.set(0, "10");
        // 显示subList后的list的第一个元素，发现也被改动为'10'
        System.out.println(list2.get(0));
        // 修改第二个subList的第二个元素
        list2.set(1, "9");
        // 显示源List的第二个元素，发现也被改动为'9'
        System.out.println(list1.get(1));
        // 删除子List的第一个元素
        Iterator<String> iterator2 = list2.listIterator();
        int index = 0;
        while (iterator2.hasNext()) {
            iterator2.next();
            if (index == 1) {
                iterator2.remove();
                break;
            }
            index ++;
        }
        // 此时源List也会被删除第二个元素
        System.out.println("list1 size: " + list1.size());
        // 删除源List的第二个元素
        list1.remove(1);
        // 此时再次调用sublist的方法，会抛出异常'java.util.ConcurrentModificationException'
        System.out.println("list2 size: " + list2.size());
    }
}
