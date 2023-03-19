package top.jolyoulu.mybatis.plugin.page;

import lombok.Data;
import top.jolyoulu.mybatis.entity.SysUser;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 14:31
 * @Description
 */
@Data
public class JlPage<T> {
    private Long page;
    private Long pageSize;
    private Long total;
    private List<T> list;
    private Boolean optimize;

    private JlPage() {
    }

    private JlPage(Long page, Long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 分页查询，执行器
     * @param clazz
     * @param page
     * @param pageSize
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T> JlPage<T> execute(Class<T> clazz,
                                        Integer page,
                                        Integer pageSize,
                                        Supplier<List<T>> supplier) {
        return init(clazz,page,pageSize).run(supplier).optimize(false);
    }

    /**
     * 分页查询，执行器
     * @param clazz
     * @param optimize
     * @param page
     * @param pageSize
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T> JlPage<T> execute(Class<T> clazz,
                                        Integer page,
                                        Integer pageSize,
                                        Supplier<List<T>> supplier,
                                        boolean optimize) {
        return init(clazz,page,pageSize).optimize(optimize).run(supplier);
    }

    /**
     * 开始分页，并且返回一个分页对象
     * @param page     当前页
     * @param pageSize 分页大小
     * @return
     */
    private static <T> JlPage<T> init(Class<T> clazz,Integer page, Integer pageSize) {
        JlPage<T> p = new JlPage<>(Long.valueOf(page), Long.valueOf(pageSize));
        JlPageLocal.set(p);
        return p;
    }

    /**
     * supplier中是正在的执行sql，get方法后会返回符合符合预期的list
     * @param supplier
     * @return
     */
    private JlPage<T> run(Supplier<List<T>> supplier) {
        JlPage<T> page = JlPageLocal.get();
        try {
            List<T> ts = supplier.get();
            page.setList(ts);
        }finally {
            JlPageLocal.remove();
        }
        return page;
    }


    /**
     * 是否开启合计优化，默认关闭
     * 优化原理，使用EXPLAIN代替COUNT函数
     * @return
     */
    private JlPage<T> optimize(boolean flag) {
        this.optimize = flag;
        return this;
    }

    /**
     * 计算起始页面
     * @return
     */
    protected Long getLimitStart() {
        //（当前页 - 1） * 分页大小 = 起始偏移量
        return ((page - 1) * pageSize);
    }
}
