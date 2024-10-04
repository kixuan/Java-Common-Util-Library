package org.xuan.ApacheCommonsPool;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SshSessionFactory extends BasePooledObjectFactory<Session> {
    @Override
    public Session create() throws Exception {
        // 1. 创建SSH会话
        JSch jsch = new JSch();
        Session session = jsch.getSession("user", "host", 22);
        session.setPassword("password");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return session;
    }

    @Override
    public PooledObject<Session> wrap(Session session) {
        return new DefaultPooledObject<>(session);
    }

    @Override
    public void destroyObject(PooledObject<Session> pooledObject) throws Exception {
        // 销毁会话
        Session session = pooledObject.getObject();
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
}
