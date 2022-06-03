package top.jolyoulu.jmmcase;

import ch.qos.logback.core.util.TimeUtil;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/30 12:50
 * @Version 1.0
 * 错误示范，由于可见性问题导致死循环
 */
@Slf4j
public class WrongTest1 {
    static int mun = 0;
    volatile static boolean read = false;
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            if (read){
                System.out.println(mun + mun);
            }else {
                System.out.println(1);
            }
        });

        Thread t2 = new Thread(() -> {
            read = true;
            mun = 2;
        });

        t1.start();
        t2.start();

    }

}