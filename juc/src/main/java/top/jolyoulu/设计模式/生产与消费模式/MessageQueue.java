package top.jolyoulu.设计模式.生产与消费模式;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 21:40
 * @Version 1.0
 */
@Slf4j
public class MessageQueue {

    private static final LinkedList<Message> list = new LinkedList<>();  //消息队列集合
    private int capacity; //队列容量

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    //接收消息
    public Message take() {
        synchronized (list) {
            while (list.isEmpty()) { //队列空，挂起等待
                try {
                    log.debug("队列空，消费者挂起等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = list.removeFirst();
            log.debug("已消费消息 {}",message);
            list.notifyAll();
            return message;
        }
    }

    //存入消息
    public void put(Message message) {
        synchronized (list) {
            while (list.size() == capacity) { //队列已满，挂起等待
                try {
                    log.debug("队列已满，生产者挂起等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);
            log.debug("已生产消息 {}",message);
            list.notifyAll();
        }
    }
}
