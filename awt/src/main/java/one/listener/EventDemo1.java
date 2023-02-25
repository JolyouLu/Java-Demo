package one.listener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/25 16:51
 * @Description
 */
public class EventDemo1 {
    Frame frame = new Frame("这里测试Event");

    TextField tf = new TextField(30);
    Button ok = new Button("确定");

    public void init() {
        MyListener listener = new MyListener();
        ok.addActionListener(listener);
        frame.add(tf);
        frame.add(ok,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private class MyListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            tf.setText("Hello World");
        }
    }

    public static void main(String[] args) {
        new EventDemo1().init();
    }

}
