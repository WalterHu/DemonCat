package test;

/**
 * @Class: TestOfCallable
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/16
 */

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 测试Callable代码.
 * Java中创建线程的方式有三种：
 * 1.继承Thread类
 * 2.实现Runnable接口
 * 3.实现Callable<T>接口，结合Future
 */
public class TestOfCallable {
    public static class CustomCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("call execution.");
            Thread.sleep(2000l);
            return "Callable";
        }
    }

    public static void main(String[] args) {
        // FutureTask代表异步执行并且获取结果。
        // 结果获取只能够通过get()方法，并且该方法在任务执行完成后才会返回，否则将阻塞。
        // FutureTask同样能够取消，但是如果执行结束后，则无法重启与取消。
        // FutureTask能够包裹Callable或者Runnable对象来完成异步任务。
        // 同样可使用Executor来执行FutureTask，或者作为Thread的target来执行任务。
        final FutureTask<String> futureTask = new FutureTask<String>(new CustomCallable());
        new Thread(futureTask).start(); // FutureTask实现了Runnable接口和Future接口
        new Thread(new Runnable() { // 新起一个线程也来获取结果
            @Override
            public void run() {
                try {
                    final long start = System.currentTimeMillis();
                    System.out.println("result of thread " + Thread.currentThread().getName() + " is "+ futureTask.get() + ", use " + (System.currentTimeMillis() - start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() { // 新起一个线程也来获取结果
            @Override
            public void run() {
                try {
                    final long start = System.currentTimeMillis();
                    System.out.println("result of thread " + Thread.currentThread().getName() + " is "+ futureTask.get() + ", use " + (System.currentTimeMillis() - start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        final long start = System.currentTimeMillis();
        try {
            System.out.println("result of thread " + Thread.currentThread().getName() + " is "+ futureTask.get() + ", use " + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
