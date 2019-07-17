package com.zzy.myapplication;

/**
 * 通过wait notify 模拟同时开始得情景
 * 这里，有1个主线程和N个子线程，每个子线程 模拟运动员，主线程模拟裁判。他们共享变量是一个开始信号
 * 我们用FireFlag来表示这个共享变量
 */
public class RacerDemo {

    /**
     * 启动10个子线程，每个线程启动后等待fire信号，主线程调用fire信号后，各个子线程才开始执行后面的操作
     *
     * @param args
     */
    public static void main(String[] args) {
        int num = 10;
        FireFlag fireFlag = new FireFlag();
        Thread[] threads = new Thread[num];
        for (int i = 0; i < num; i++) {
            threads[i] = new Racer(fireFlag);
            threads[i].start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fireFlag.fire();
    }

    static class FireFlag {
        private volatile boolean fired = false;

        /**
         * 子线程调用，等待枪响
         *
         * @throws InterruptedException
         */
        public synchronized void waitForFire() throws InterruptedException {
            while (!fired) {
                wait();
            }
        }

        /**
         * 主线程调用,发射比赛开始得信号
         */
        public synchronized void fire() {
            fired = true;
            notifyAll();
        }
    }

    /**
     * 运动员 线程
     */
    static class Racer extends Thread {
        FireFlag fireFlag;

        public Racer(FireFlag fireFlag) {
            this.fireFlag = fireFlag;
        }

        @Override
        public void run() {
            try {
                fireFlag.waitForFire();
                System.out.println("thread name: " + Thread.currentThread().getName() + " startRun()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
