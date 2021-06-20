package top.jolyoulu.zookeepr_curator.znode;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 20:15
 * @Version 1.0
 */
public class CuratorCrudTest {

    @Test
    public void CuratorCrudTest(){
        CuratorCrud curatorCrud = new CuratorCrud();
        curatorCrud.createPersistent("/CuratorCrudTest","12312");
        System.out.println(curatorCrud.getData("/CuratorCrudTest"));
        curatorCrud.delete("/CuratorCrudTest");
    }

}