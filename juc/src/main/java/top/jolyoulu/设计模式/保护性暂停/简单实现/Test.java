package top.jolyoulu.设计模式.保护性暂停.简单实现;

import lombok.extern.slf4j.Slf4j;
import top.jolyoulu.utils.Downloader;

import java.io.IOException;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 15:23
 * @Version 1.0
 * 保护性暂停-简单案例
 */
@Slf4j
public class Test{
    public static void main(String[] args) {
        GuardedObject<List<String>> guardedObject = new GuardedObject<>();

        new Thread(() -> {
            //wait等待guardedObject有结果
            List<String> list = guardedObject.get();
            log.debug("收到结果{}",list);
        },"t1").start();

        new Thread(() ->{
            try {
                //执行耗时任务获取结果
                log.debug("执行任务，访问百度网页");
                List<String> download = Downloader.download();
                guardedObject.complete(download);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}
