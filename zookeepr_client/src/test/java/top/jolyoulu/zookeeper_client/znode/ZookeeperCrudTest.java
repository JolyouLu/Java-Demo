package top.jolyoulu.zookeeper_client.znode;

import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 14:54
 * @Version 1.0
 */
public class ZookeeperCrudTest {

    @Test
    public void testCrud() throws KeeperException, InterruptedException {
        ZookeeperCrud zookeeperCrud = new ZookeeperCrud();
        System.out.println(zookeeperCrud);
        //删除
        if (null != zookeeperCrud.exists("/PersistentNode")){
            zookeeperCrud.delete("/PersistentNode");
        }
        //测试创建持久节点
        zookeeperCrud.createPersistent("/PersistentNode","123123");
        //测试创建临时节点
        zookeeperCrud.createEphemeral("/EphemeralNode","123123");
        //打印节点信息
        System.out.println(zookeeperCrud.getData("/EphemeralNode"));
    }

}