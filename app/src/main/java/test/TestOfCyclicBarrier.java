package test;

/**
 * @Class: TestOfCyclicBarrier
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/19
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 测试CyclicBarrier
 * 字面意思是回环栅栏，通过它可以实现让一组线程等待至某个状态之后在全部同时执行。
 * 叫做回环是因为所有等待线程都被释放以后，CyclicBarrier还可以被重用。
 * 我们暂且把这个状态叫做barrier，当调用await()方法之后，线程就处于barrier了。
 *
 * 与CountdownLatch不同，CyclicBarrier可以重用
 */
public class TestOfCyclicBarrier {

    public static void main(String[] args) {
//        new TestOfCyclicBarrier().testOfCyclicBarrier();
        new TestOfCyclicBarrier().testOfCyclicBarrierExtra();
    }

    /**
     * 线程Thread-0正在执行写入操作...
     线程Thread-1正在执行写入操作...
     线程Thread-2正在执行写入操作...
     线程Thread-3正在执行写入操作...
     线程Thread-0写入完毕，等待其他线程完毕
     线程Thread-1写入完毕，等待其他线程完毕
     线程Thread-2写入完毕，等待其他线程完毕
     线程Thread-3写入完毕，等待其他线程完毕
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...

     从输出结果来看，每个线程都在等待其他线程执行完毕，
     然后执行后续动作
     */
    private void testOfCyclicBarrier() {
        int n = 4; // 4个线程
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(n);
        for (int i = 0; i < n; i ++) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println("线程" + Thread.currentThread().getName() + "正在执行写入操作...");
                    try {
                        Thread.sleep(5000l);
                        System.out.println("线程" + Thread.currentThread().getName() + "写入完毕，等待其他线程完毕");
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println("所有线程执行完毕，继续处理后续动作...");
                }
            }.start();
        }
    }

    /**
     * 如果想在所有线程执行完毕后再执行一段额外操作，可以使用
     * Runnable参数
     *
     * 线程Thread-0正在执行写入操作...
     线程Thread-1正在执行写入操作...
     线程Thread-2正在执行写入操作...
     线程Thread-3正在执行写入操作...
     线程Thread-1写入完毕，等待其他线程完毕
     线程Thread-3写入完毕，等待其他线程完毕
     线程Thread-2写入完毕，等待其他线程完毕
     线程Thread-0写入完毕，等待其他线程完毕
     线程Thread-3执行额外操作
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...
     所有线程执行完毕，继续处理后续动作...

     从输出结果来看，当线程都达到barrier之后，会选择一个线程执行额外操作
     */
    private void testOfCyclicBarrierExtra() {
        int n = 4; // 4个线程
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(n, new Runnable() {
            @Override
            public void run() {
                System.out.println("线程" + Thread.currentThread().getName() + "执行额外操作");
            }
        });
        for (int i = 0; i < n; i ++) {
            new Thread() {
                @Override
                public void run() {
                    System.out.println("线程" + Thread.currentThread().getName() + "正在执行写入操作...");
                    try {
                        Thread.sleep(5000l);
                        System.out.println("线程" + Thread.currentThread().getName() + "写入完毕，等待其他线程完毕");
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println("所有线程执行完毕，继续处理后续动作...");
                }
            }.start();
        }
    }
}
