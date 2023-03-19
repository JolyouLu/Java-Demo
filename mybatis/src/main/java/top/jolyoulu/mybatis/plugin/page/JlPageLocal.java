package top.jolyoulu.mybatis.plugin.page;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 17:35
 * @Description
 */
public class JlPageLocal {

    private static final ThreadLocal<JlPage<?>> PAGE_LOCAL = new ThreadLocal<>();

    protected static <T> JlPage<T> get() {
        return (JlPage<T>) PAGE_LOCAL.get();
    }

    protected static <T> void set(JlPage<T> page) {
        PAGE_LOCAL.set(page);
    }

    protected static void remove() {
        PAGE_LOCAL.remove();
    }
}
