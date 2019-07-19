package com.zzy.myapplication;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerCustomerDemo2 {

    public static void main(String [] args){

    }

    static class MyQueue<E>{
        private Queue<E> queue =null;
        private int limit ;
        private Lock lock = new ReentrantLock();
        private Condition notFull = lock.newCondition();
        private Condition notEmpty = lock.newCondition();
        public MyQueue(int limit){
            this.limit = limit;
            queue = new ArrayDeque<>();
        }

        public void put(E e) throws InterruptedException {
            lock.lockInterruptibly();
            try{
                while (queue.size()==limit){
                    notFull.await();
                }
                queue.add(e);
                notEmpty.signal();
            }finally {
                lock.unlock();
            }
        }

        public E take() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (queue.isEmpty()){
                    notEmpty.await();
                }
                E poll = queue.poll();
                notFull.signal();
                return poll;
            }finally {
                lock.unlock();
            }
        }
    }
}
