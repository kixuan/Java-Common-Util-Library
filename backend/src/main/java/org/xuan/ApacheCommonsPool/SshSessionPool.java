package org.xuan.ApacheCommonsPool;

import com.jcraft.jsch.Session;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class SshSessionPool {
    private static GenericObjectPool<Session> pool;

    private SshSessionPool() {}

    public static GenericObjectPool<Session> getInstance() {
        if (pool == null) {
            synchronized (SshSessionPool.class) {
                if (pool == null) {
                    SshSessionFactory factory = new SshSessionFactory();
                    pool = new GenericObjectPool<>(factory);
                    pool.setMaxTotal(10); // 最大连接数
                    pool.setMaxIdle(5);   // 最大空闲连接数
                    pool.setMinIdle(2);   // 最小空闲连接数
                    pool.setTestOnBorrow(true); // 借用时测试连接是否可用
                }
            }
        }
        return pool;
    }
}
