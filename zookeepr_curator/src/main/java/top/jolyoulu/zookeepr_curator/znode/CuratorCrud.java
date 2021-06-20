package top.jolyoulu.zookeepr_curator.znode;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 20:08
 * @Version 1.0
 */
public class CuratorCrud {

    public CuratorFramework cf;

    public CuratorCrud() {
        //自动重连设置 初试时间1S，重试10次
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 10);
        cf = CuratorFrameworkFactory.builder()
                .connectString("192.168.100.101:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(retry)
                .build();
        cf.start();
    }


    /**
     * 创建一个无序持久节点
     * @param path
     * @param data
     * @return
     */
    public String createPersistent(String path,String  data){
        try {
            cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    /**
     * 获取节点数据
     * @param path
     * @return
     */
    public String getData(String path){
        try {
            return new String(cf.getData().forPath(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * 删除节点
     * @param path
     */
    public void delete(String path){
        try {
            cf.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 修改节点
     * @param path
     * @param data
     */
    public void setData(String path,String  data){
        try {
            cf.setData().forPath(path,data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
