package top.jolyoulu.mybatis.plugin.page;

import lombok.Data;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 14:48
 * @Description
 */
@Data
public class PageBO {
    private Long page = 1L;
    private Long pageSize = 1L;


    public class Test extends PageBO{

    }

    public void main(String[] args) {
        JlPage<Test> page1 = JlPage.getPage(new Test());
    }
}
