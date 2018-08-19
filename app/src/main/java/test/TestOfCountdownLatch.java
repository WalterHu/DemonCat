package test;

/**
 * @Class: TestOfCountdownLatch
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/19
 */

import java.util.concurrent.CountDownLatch;

/**
 * 测试CountdownLatch
 * CountdownLatch可以实现类似计数功能。
 * 比如有一个任务A，它要等待其他4个任务执行完毕后才能执行，此时
 * 就可以利用CountdownLatch来实现这种功能了。
 *
 * CountdownLatch不能够重用。
 */
public class TestOfCountdownLatch {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(2); // 计数值2

        new Thread() {
            @Override
            public void run() {
                System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                try {
                    Thread.sleep(3000l);
                    System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                System.out.println("子线程" + Thread.currentThread().getName() + "开始执行");
                try {
                    Thread.sleep(3000l);
                    System.out.println("子线程" + Thread.currentThread().getName() + "执行完毕");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("等待子线程执行完毕");
        try {
            countDownLatch.await();
            System.out.println("子线程执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
