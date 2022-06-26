package top.jolyoulu.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/25 12:04
 * @Version 1.0
 */
@Slf4j
public class AtomicMarkableReferenceDemo {
    public static void main(String[] args) {
        //初始化一个容器，并且设置标记为true可更换
        AtomicMarkableReference<ArrayList> ref = new AtomicMarkableReference<>(new ArrayList<>(10), true);
        log.debug("start...");
        ArrayList prev = ref.getReference();
        //装数据
        for (int i = 0; i < 10; i++) {
            prev.add(1);
        }
        log.debug("想更换一个新的容器？");
        log.debug("更换前容器内容 {}",prev);
        //cas 更换对象，并且将标记从true 改为false
        ref.compareAndSet(prev,new ArrayList<>(),true,false);
        log.debug("更换后容器内容 {}",ref.getReference());
    }
}
