package top.jolyoulu.zookeeper_client.acl;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import top.jolyoulu.zookeeper_client.watcher.ZookeeperWatcher;

import static org.junit.Assert.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 17:02
 * @Version 1.0
 */
public class ZookeeperAclTest {

    @Test
    public void testCrudAcl() throws KeeperException, InterruptedException {
        //开启认证连接Zookeeper
        ZookeeperAcl zookeeperAcl = new ZookeeperAcl(true);
        //删除
        if (null != zookeeperAcl.exists("/ZookeeperAcl")){
            zookeeperAcl.delete("/ZookeeperAcl");
        }
        //创建带权限的值
        zookeeperAcl.createPersistentAcl("/ZookeeperAcl","abd");
    }

}