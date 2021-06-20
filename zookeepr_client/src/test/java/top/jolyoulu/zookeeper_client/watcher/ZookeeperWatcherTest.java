package top.jolyoulu.zookeeper_client.watcher;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import top.jolyoulu.zookeeper_client.znode.ZookeeperCrud;

import static org.junit.Assert.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 16:25
 * @Version 1.0
 */
public class ZookeeperWatcherTest {

    @Test
    public void testCrudWatcher() throws KeeperException, InterruptedException {
        ZookeeperWatcher zookeeperWatcher = new ZookeeperWatcher();
        System.out.println(zookeeperWatcher);
        //删除
        if (null != zookeeperWatcher.exists("/PersistentNode",true)){
            zookeeperWatcher.delete("/PersistentNode");
        }

        //测试创建持久节点
        zookeeperWatcher.createPersistent("/PersistentNode","123123");
        //测试创建临时节点
        zookeeperWatcher.createEphemeral("/EphemeralNode","123123");
        //打印节点信息
        System.out.println(zookeeperWatcher.getData("/EphemeralNode",true));
    }

}