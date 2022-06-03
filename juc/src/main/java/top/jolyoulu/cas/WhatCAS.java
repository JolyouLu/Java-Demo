package top.jolyoulu.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

interface Account {
    //并发测试方法：创建1000个线程同时调用withdraw方法，每个线程扣除10元，最后得出结果
    static void testDemo(Account account) {
        ArrayList<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.currentTimeMillis();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("余额：" + account.getBalance() + " 耗时：" + (end - start) + "ms");
    }

    //获取余额
    Integer getBalance();

    //扣除金额
    void withdraw(Integer amount);
}

/**
 * @Author: JolyouLu
 * @Date: 2022/6/3 16:27
 * @Version 1.0
 */
@Slf4j
public class WhatCAS {
    public static void main(String[] args) {
        Account account = new CAS(10000);
        Account.testDemo(account);
    }
}

//使用cas实现无锁并发
class CAS implements Account{
    //juc包提供的原子操作类
    private AtomicInteger balance;

    public CAS(Integer balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true){
            //获取当前账户余额
            int perv = balance.get();
            //计算扣除后的值
            int next = perv - amount;
            //使用比较替换算法共享账户余额，更新成功后跳出循环
            if (balance.compareAndSet(perv,next)){
                break;
            }
        }
    }
}

//使用synchronized实现线程安全的扣除账户
class AddLock implements Account {
    private Integer balance;

    public AddLock(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}
