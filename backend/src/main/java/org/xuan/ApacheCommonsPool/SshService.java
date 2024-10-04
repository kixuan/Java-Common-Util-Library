package org.xuan.ApacheCommonsPool;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class SshService {
    public void executeRemoteCommand() {
        Session session = null;
        try {
            session = SshSessionPool.getInstance().borrowObject();
            // 执行远程命令
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("ls -la");
            channel.connect();
            // 处理命令输出...
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                SshSessionPool.getInstance().returnObject(session);
            }
        }
    }
}
