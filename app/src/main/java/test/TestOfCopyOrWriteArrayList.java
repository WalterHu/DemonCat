package test;

/**
 * @Class: TestOfCopyOrWriteArrayList
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试CopyOnWriteArrayList
 */
public class TestOfCopyOrWriteArrayList {
    public static void main(String[] args) {
        int index = 0;
        // 1.初始化List内容
        List<Integer> arrayList = Arrays.asList(new Integer[] {1, 2, 3});
        List<Integer> list = new CopyOnWriteArrayList<>(arrayList);
        // 2.模拟多线程对list进行读和写
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new ReadThread(list, ++index));
        executorService.execute(new WriteThread(list, ++index));
        executorService.execute(new ReadThread(list, ++index));
        executorService.execute(new WriteThread(list, ++index));
        executorService.execute(new ReadThread(list, ++index));
        executorService.execute(new WriteThread(list, ++index));

        System.out.println("copyList size:" + list.size());
        // 运行上述代码，没有出现java.util.ConcurrentModificationException
        // 说明CopyOnWriteArrayList是线程安全的
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<String>());
    }
}
