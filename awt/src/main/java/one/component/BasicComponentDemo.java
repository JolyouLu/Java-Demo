package one.component;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 15:56
 * @Description
 */
public class BasicComponentDemo {
    Frame frame = new Frame("测试基本组件");
    //多行文本域
    TextArea ta = new TextArea(5, 20);
    //下来选择框
    Choice colorChooser = new Choice();
    //单选
    CheckboxGroup dbg = new CheckboxGroup();
    Checkbox male = new Checkbox("男", dbg, true);
    Checkbox female = new Checkbox("女", dbg, false);
    //复选框
    Checkbox isMarried = new Checkbox("是否已婚？");
    //单行文本框
    TextField tf = new TextField(50);
    //确认按钮
    Button ok = new Button("确认");
    //列表框
    List colorList = new List(6, true);

    public void init() {
        //组装界面
        Box bBox = Box.createHorizontalBox();
        bBox.add(tf);
        bBox.add(ok);
        frame.add(bBox, BorderLayout.SOUTH);

        Box cBox = Box.createHorizontalBox();
        colorChooser.add("红色");
        colorChooser.add("蓝色");
        colorChooser.add("绿色");
        cBox.add(colorChooser);
        cBox.add(male);
        cBox.add(female);
        cBox.add(isMarried);

        Box topLeft = Box.createVerticalBox();
        topLeft.add(ta);
        topLeft.add(cBox);

        Box top = Box.createHorizontalBox();
        colorList.add("红色");
        colorList.add("蓝色");
        colorList.add("绿色");
        top.add(topLeft);
        top.add(colorList);
        frame.add(top,BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BasicComponentDemo().init();
    }
}
