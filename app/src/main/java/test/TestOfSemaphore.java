package test;

/**
 * @Class: TestOfSemaphore
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/19
 */

import java.util.concurrent.Semaphore;

/**
 * Semaphore字面意思为信号量。它可以控制同时访问资源的线程个数，
 * 通过acquire()方法获取permit，release()方法释放permit。
 *
 */
public class TestOfSemaphore {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 8; i ++)
            new Worker(semaphore, i).start();
    }

    private static class Worker extends Thread {
        private Semaphore semaphore;
        private int index;

        public Worker(Semaphore semaphore, int index) {
            this.semaphore = semaphore;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人" + index + "正在占用机器生产");
                Thread.sleep(2000l);
                System.out.println("工人" + index + "生产完毕，释放机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
