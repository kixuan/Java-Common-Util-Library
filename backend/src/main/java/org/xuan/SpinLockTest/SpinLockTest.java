package org.xuan.SpinLockTest;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 实现一个简单的自旋锁
 * <p>
 * 但是这种实现会造成某些线程一直都未获取到锁造成线程饥饿，无法保证公平性
 * ==》 TicketLock 是采用排队叫号的机制来实现的一种公平锁，保证公平但性能开销过大
 * ==》 CLHLock 和 MCSLock【怎么越学越多，越学越复杂(((φ(◎ロ◎;)φ)))】
 * @author 醒酒器
 */
public class SpinLockTest {
    private final AtomicBoolean available = new AtomicBoolean(false);

    // 获取锁，支持中断处理
    public void lock() {
        while (!tryLock()) {
            // 让出 CPU，以避免过度消耗
            Thread.yield();
            // 处理线程中断
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException("线程被中断，获取锁失败");
            }
        }
    }

    // 尝试获取锁，返回是否成功
    public boolean tryLock() {
        return available.compareAndSet(false, true);
    }

    // 释放锁
    public void unlock() {
        if (!available.compareAndSet(true, false)) {
            throw new IllegalStateException("释放锁失败，锁状态不一致");
        }
    }

    public static void main(String[] args) {
        SpinLockTest spinLockTest = new SpinLockTest();

        // 模拟线程获取锁和释放锁
        Thread t1 = new Thread(() -> {
            spinLockTest.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 获取到锁");
                // 模拟处理任务
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                spinLockTest.unlock();
                System.out.println(Thread.currentThread().getName() + " 释放锁");
            }
        });

        Thread t2 = new Thread(() -> {
            spinLockTest.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " 获取到锁");
            } finally {
                spinLockTest.unlock();
                System.out.println(Thread.currentThread().getName() + " 释放锁");
            }
        });

        t1.start();
        t2.start();
    }
}
