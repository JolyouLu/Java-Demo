package one.layout;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 14:34
 * @Description
 * FlowLayout流式布局，组件沿着指定方向流动
 */
public class FlowLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试FlowLayout");
        //修改布局管理
        frame.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        //添加多个按钮
        for (int i = 1; i <= 100; i++) {
            frame.add(new Button("按钮"+i));
        }
        //设置最佳大小
        frame.pack();
        frame.setVisible(true);
    }
}
