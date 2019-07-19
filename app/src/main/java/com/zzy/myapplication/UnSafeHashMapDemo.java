package com.zzy.myapplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟高并发情况下，hashmap 出现死循环的问题
 */
public class UnSafeHashMapDemo {

    public static void main(String[] args) {
        unsafeUpdate();
//        unsafeConcurrentUpdate();
    }

    public static void unsafeUpdate() {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();

        int num = 1000;
        Thread[] threads = new Thread[num];
        for (int i = 0; i < num; i++) {
            threads[i] = new SitumationThread(map);
            threads[i].start();
        }
    }

    static class SitumationThread extends Thread {
        Map<Integer, Integer> map;

        public SitumationThread(Map<Integer, Integer> map) {
            this.map = map;
        }

        @Override
        public void run() {
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                map.put(random.nextInt(), 1);
            }
        }
    }

    public static void unsafeConcurrentUpdate() {
        final Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < 1000; i++) {
            Thread t = new Thread() {
                Random rnd = new Random();
                @Override
                public void run() {
                    for(int i = 0; i < 1000; i++) {
                        map.put(rnd.nextInt(), 1);
                    }
                }
            };
            t.start();
        }
    }
}
