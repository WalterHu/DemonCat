package test;

import java.util.List;

/**
 * @Class: ReadThread
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class ReadThread implements Runnable {
    private List<Integer> list;
    private int index;

    public ReadThread(List<Integer> list, int index) {
        this.list = list;
        this.index = index;
    }

    @Override
    public void run() {
        for (Integer e : list)
        System.out.println("thread " + index + " read element: " + e);
    }
}
