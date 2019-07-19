package com.zzy.myapplication;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitThread extends Thread {


    public static void main(String[] args) {
        WaitThread thread = new WaitThread();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.fire();
    }

    private volatile boolean fired = false;


    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    @Override
    public void run() {
        lock.lock();
        try {
            while (!fired) {
                condition.await();
            }
            System.out.println("exit");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void fire() {
        lock.lock();
        try {
            fired = true;
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
