package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Class: TestOfArrayBlockingQueue
 * @Description: java类作用描述
 * @Author: hubohua
 * @CreateDate: 2018/8/27
 */
public class TestOfArrayBlockingQueue {
    static BlockingQueue<Product> queue = new ArrayBlockingQueue<Product>(1);

    public static void main(String[] args) {
        final Product pro = new Product();
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                pro.product(queue);
            }
        });
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                pro.consume(queue);
            }
        });
        producer.start();
        consumer.start();
    }
}

class Product {
    public void consume(BlockingQueue<Product> queue) {
        if (queue != null) {
            Product product = null;

            try {
                while ((product = queue.take()) != null) { // 阻塞如果空了
                    Thread.sleep(2000l);
                    System.out.println("顾客 " + Thread.currentThread().getName() +
                            " 消耗了一个product: " + product + ", 剩余: " + queue.size());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void product(BlockingQueue<Product> queue) {
        if (queue != null) {
            Product product = new Product();
            while (queue.offer(product)) { // 阻塞如果满了
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("工厂 " + Thread.currentThread().getName() +
                        " 生产了一个product: " + product + ", 剩余: " + queue.size());
            }
        }
    }
}
