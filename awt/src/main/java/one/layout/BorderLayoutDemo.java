package one.layout;

import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 14:41
 * @Description
 * BorderLayout容器布局
 * 通过指定组件区域：NORTH(上) CENTER(中) SOUTH(下) WEST(左) EAST(右)
 */
public class BorderLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试BorderLayout");
        //给frame设置BroderLayout布局管理器
        frame.setLayout(new BorderLayout(30,10));
        //往frame的指定区域添加组件
        frame.add(new Button("北侧按钮"),BorderLayout.NORTH);
        frame.add(new Button("南侧按钮"),BorderLayout.SOUTH);
        frame.add(new Button("东侧按钮"),BorderLayout.EAST);
        frame.add(new Button("西侧按钮"),BorderLayout.WEST);
        frame.add(new Button("中间按钮"),BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
