package top.jolyoulu.mybatis.plugin.page;

import lombok.Data;
import top.jolyoulu.mybatis.entity.SysUser;

import java.util.List;

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
    private boolean optimize;

    private JlPage() {
    }

    private JlPage(Long page, Long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    /**
     * 获取一个分页对象
     * @param bo 传入一个继承与PageBO的类
     * @return
     * @param <T>
     */
    public static <T extends PageBO> JlPage<T> getPage(T bo){
        return new JlPage<T>(bo.getPage(),bo.getPageSize());
    }

    /**
     * 获取一个分页对象
     * @param tClass 泛型类
     * @param page 当前页
     * @param pageSize 分页大小
     * @return
     * @param <T>
     */
    public static <T> JlPage<T> getPage(Class<T> tClass, Long page, Long pageSize){
        return new JlPage<T>(page,pageSize);
    }

    /**
     * 是否开启合计优化，默认关闭
     * 优化原理，使用EXPLAIN代替COUNT函数
     * @return
     */
    public JlPage<T> optimize(boolean flag) {
        this.optimize = flag;
        return this;
    }

    /**
     * 计算起始页面
     * @return
     */
    public Long getLimitStart() {
        //（当前页 - 1） * 分页大小 = 起始偏移量
        return ((page - 1) * pageSize);
    }
}
