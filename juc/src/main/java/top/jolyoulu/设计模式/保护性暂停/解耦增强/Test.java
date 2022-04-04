package top.jolyoulu.设计模式.保护性暂停.解耦增强;

import lombok.extern.slf4j.Slf4j;
import top.jolyoulu.utils.Downloader;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 15:23
 * @Version 1.0
 * 保护性暂停-简单案例
 */
@Slf4j
public class Test{
    public static void main(String[] args) throws InterruptedException {
        //模拟3个居民
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        TimeUnit.SECONDS.sleep(3);
        //获取所以待派发的信
        Set<String> set = Futures.getIds();
        for (String id : set) {
            new Postman(id,"Hello World！").start();
        }
    }
}
//居民，专门收信
@Slf4j
class People extends Thread{
    @Override
    public void run() {
        GuardedObject go = Futures.createGuardedObject();
        log.debug("邮箱 id:{}",go.getId());
        Object res = go.get();
        log.debug("收信 id:{}, 内容:{}",go.getId(),res);
    }
}
//邮递员，专门派件
@Slf4j
class Postman extends Thread{

    private String id;
    private String msg;

    public Postman(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public void run() {
        GuardedObject go = Futures.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}",id,msg);
        go.complete(msg);
    }
}

