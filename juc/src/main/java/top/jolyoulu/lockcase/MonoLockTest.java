package top.jolyoulu.lockcase;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/5 21:29
 * @Version 1.0
 */
public class MonoLockTest {
    public static void main(String[] args) {
        Room room = new Room();
        new Thread(() -> {room.sleep();},"张三").start();
        new Thread(() -> {room.study();},"李四").start();
    }

    @Slf4j
    static class Room{
        private final Object sleepRoom = new Object();
        private final Object studRoom = new Object();
        @SneakyThrows
        public void sleep(){
            synchronized (sleepRoom){
                log.debug("sleeping 2小时");
                TimeUnit.SECONDS.sleep(2);
            }
        }
        @SneakyThrows
        public void study(){
            synchronized (studRoom){
                log.debug("studying 1小时");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
