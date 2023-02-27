package one.basic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:43
 * @Description
 */
public class InputDialogTest {
    JFrame jf = new JFrame("测试输入对话框");
    JTextArea jta = new JTextArea(6,30);

    //声明按钮
    JButton btn = new JButton(new AbstractAction("弹出输入对话框") {
        @Override
        public void actionPerformed(ActionEvent e) {
            String result = JOptionPane.showInputDialog(jf, "请填写您的银行账号：", "输入对话框", JOptionPane.INFORMATION_MESSAGE);
            jta.append(result);
        }
    });

    public void init(){
        jf.add(new JScrollPane(jta));
        jf.add(btn, BorderLayout.SOUTH);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new InputDialogTest().init();
    }
}
