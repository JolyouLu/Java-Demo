package one.layout;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 15:36
 * @Description
 *
 */
public class BoxLayoutDemo2 {
    public static void main(String[] args) {
        Frame frame = new Frame("这里测试BoxLayout");
        //创建水平排序的box
        Box hBox = Box.createHorizontalBox();
        //添加2个按钮
        hBox.add(new Button("水平按钮1"));
        hBox.add(new Button("水平按钮2"));
        //创建垂直排序box
        Box vBox = Box.createVerticalBox();
        //添加2个按钮
        vBox.add(new Button("垂直按钮1"));
        vBox.add(new Button("垂直按钮2"));
        //吧2个box添加到frame
        frame.add(hBox,BorderLayout.NORTH);
        frame.add(vBox,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
