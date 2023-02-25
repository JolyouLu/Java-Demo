package one.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 16:51
 * @Description
 */
public class ListenerDemo1 {

    public static void main(String[] args) {
        Frame frame = new Frame("这里测试监听器");

        TextField tf = new TextField(30);
        Choice names = new Choice();
        names.add("111");
        names.add("222");
        names.add("333");
        //k监听文本框变化
        tf.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                String text = tf.getText();
                System.out.println("当前文本框中的内容：" + text);
            }
        });
        //监听条目变化
        names.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object item = e.getItem();
                System.out.println("当前选中的条目：" + item);
            }
        });
        //给frame注册ContainerListener监听器，监听容器中组件的添加
        frame.addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                Component child = e.getChild();
                System.out.println("读取frame中添加了：" + child);
            }

            @Override
            public void componentRemoved(ContainerEvent e) {

            }
        });

        Box hBox = Box.createHorizontalBox();
        hBox.add(names);
        hBox.add(tf);
        frame.add(hBox);

        frame.pack();
        frame.setVisible(true);
    }

}
