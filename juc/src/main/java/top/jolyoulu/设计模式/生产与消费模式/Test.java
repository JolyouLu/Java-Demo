package top.jolyoulu.设计模式.生产与消费模式;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 22:01
 * @Version 1.0
 */
@Slf4j
public class Test {
    public static void main(String[] args) {

        MessageQueue queue = new MessageQueue(2);

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id,"值"+id));
            },"生产者"+(i+1)).start();
        }

        new Thread(() -> {
            while (true){
                Message take = queue.take();
            }
        },"消费者").start();
    }
}
