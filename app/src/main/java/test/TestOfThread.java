package test;

/**
 * @Class: TestOfThread
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/17
 */

/**
 * Thread测试
 * Thread线程生命周期：创建-就绪-运行-阻塞-结束
 */
public class TestOfThread {
    public static void main(String[] args) {
        TestOfThread testOfThread = new TestOfThread();
        // testOfThread.testofJoin();
        // testOfThread.testOfYield();
        testOfThread.testOfInterrupt();
    }

    private void testofJoin() {
        // join()方法会阻塞当前线程以等待其他线程结束。
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Thread start execute.");
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        final long start = System.currentTimeMillis();
        // another thread
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread.join();
                    System.out.println("Thread 1 start execute wait: " + (System.currentTimeMillis() - start));
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Current thread print wait: " + (System.currentTimeMillis() - start));
    }

    private void testOfYield() {
        // yield()方法同sleep方法一样，会停止当前线程的运行状态，
        // 但是sleep会阻塞线程，进入block状态，而yield会让线程重新进入
        // 就绪状态，所以会出现，调用了yield之后会立即被JVM调起重新进入运行态
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 2 execute start.");
                Thread.yield();
                System.out.println("thread 2 execute finish.");
            }
        });
        thread2.start();
    }

    private void testOfInterrupt() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 10000;
                while(--i >= 0) {
                    System.out.println("thread execute. " + i);
                }
                System.out.println("<<====thread execute finish====>>");
            }
        });
        thread.start();
        try {
            thread.interrupt(); // call interrupt to test thread
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 这里调用了以后并没有打断thread输出，该方法仅仅是改变起interrupted标志位
    }
}
