package com.zzy.myapplication;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 通过wait/notify方式实现生产者消费者模式
 */
public class ProducerCustomerDemo {

    public static void main(String[] args) {
        MyQueue<String> myQueue = new MyQueue<>(10);
        new Producer(myQueue).start();
        new Customer(myQueue).start();
    }

    /**
     * 共享队列。该队列是有个数限制的队列，限制条件通过构造方法传递
     *
     * @param <E>
     */
    static class MyQueue<E> {
        private int limit;
        private Queue<E> queue;

        public MyQueue(int limit) {
            this.limit = limit;
            queue = new ArrayDeque<>(limit);
        }

        /**
         * wait/notify 只能在synchronized代码块中被调用，调用wait会释放锁
         * 生产者线程往队列添加数据，如果队列满了，则wait等待被唤醒
         *
         * @param e
         * @throws InterruptedException
         */
        public synchronized void put(E e) throws InterruptedException {
            if (queue.size() == limit) {
                wait();
            }
            queue.add(e);
            notifyAll();
        }

        /**
         * wait/notify 只能在synchronized代码块中被调用，调用wait会释放锁
         * 消费者线程从队列取出数据，如果队列为空，那么wait 等待被唤醒
         *
         * @return
         * @throws InterruptedException
         */
        public synchronized E take() throws InterruptedException {
            if (queue.isEmpty()) {
                wait();
            }
            notifyAll();
            return queue.poll();
        }

    }

    /**
     * 生产者线程
     */
    static class Producer extends Thread {
        MyQueue<String> queue;

        public Producer(MyQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            int num = 0;
            while (true) {
                try {
                    String task = String.valueOf(num);
                    queue.put(task);
                    num++;
                    System.out.println("Producer task: " + task);
                    Thread.sleep((int) Math.random() * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消费者线程
     */
    static class Customer extends Thread {
        MyQueue<String> queue;

        public Customer(MyQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                String task = null;
                try {
                    task = queue.take();
                    System.out.println("Customer task: " + task);
                    Thread.sleep((int) Math.random() * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }

}
