package one.basic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:43
 * @Description
 */
public class MessageDialogTest {
    JFrame jf = new JFrame("测试消息对话框");
    JTextArea jta = new JTextArea(6,30);

    //声明按钮
    JButton btn = new JButton(new AbstractAction("弹出消息对话框") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //弹出消息对话框，显示文本域中输入的内容
            String text = jta.getText();
            JOptionPane.showMessageDialog(jf,text,"消息对话框",JOptionPane.INFORMATION_MESSAGE);
        }
    });

    public void init(){
        jf.add(jta);
        jf.add(btn, BorderLayout.SOUTH);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new MessageDialogTest().init();
    }
}
