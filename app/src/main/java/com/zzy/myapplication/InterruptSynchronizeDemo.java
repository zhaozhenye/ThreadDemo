package com.zzy.myapplication;

/**
 * test 方法在持有lock的情况下启动线程a，而线程a也去尝试获取lock，所以会进入锁等待队列
 * 随后test调用线程a的interrupt方法并调用json等待线程a结束，线程会结束吗？不会
 * interrupt 方法只会设置线程的中断标志，而不会把他从等待队列中取出来。
 * 这就是synchronize的缺陷：在获取锁的过程不响应中断请求。
 */
public class InterruptSynchronizeDemo {

    private static Object lock = new Object();

    public static void main(String [] args) {
        try {
            test();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void test() throws InterruptedException {
        synchronized (lock) {
            A a = new A();
            a.start();
            Thread.sleep(1000);
            a.interrupt();
            a.join();
        }

    }

    static class A extends Thread {
        @Override
        public void run() {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {

                }


            }
            System.out.println("exit");

        }
    }
}
