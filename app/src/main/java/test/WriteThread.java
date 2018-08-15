package test;

import java.util.List;

/**
 * @Class: WriteThread
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/15
 */
public class WriteThread implements Runnable {
    private List<Integer> list;
    private int index;

    public WriteThread(List<Integer> list, int index) {
        this.list = list;
        this.index = index;
    }

    @Override
    public void run() {
        this.list.add(10);
        System.out.println("thread " + index + " put in to list.");
    }
}
