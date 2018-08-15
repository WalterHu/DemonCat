package test;

/**
 * @Class: TestOfEmptyCollection
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 测试返回空集合
 */
public class TestOfEmptyCollection {
    public static void main(String[] args) {
        List<String> emptyList = Collections.emptyList();
        System.out.println("emptyList size: " + emptyList.size());
        // 向空列表添加元素对象
        emptyList.add("empty"); // java.lang.UnsupportedOperationException
        // java.util.AbstractList.add(AbstractList.java:148)
        // 无法向Collection.emptyXXX()返回的集合中添加对象元素，它们是不可修改的集合
    }
}
