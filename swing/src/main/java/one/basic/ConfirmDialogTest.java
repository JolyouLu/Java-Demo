package one.basic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:43
 * @Description
 */
public class ConfirmDialogTest {
    JFrame jf = new JFrame("测试确认对话框");
    JTextArea jta = new JTextArea(6,30);

    //声明按钮
    JButton btn = new JButton(new AbstractAction("弹出确认对话框") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //弹出消息对话框，显示文本域中输入的内容
            String text = jta.getText();
            jta.append("\n");
//            int result = JOptionPane.showConfirmDialog(jf, text, "消息对话框", JOptionPane.DEFAULT_OPTION);
            int result = JOptionPane.showConfirmDialog(jf, text, "消息对话框", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                jta.append("用户点击了 是 按钮\n");
            }
            if (result == JOptionPane.NO_OPTION){
                jta.append("用户点击了 否 按钮\n");
            }
            if (result == JOptionPane.OK_OPTION){
                jta.append("用户点击了 确认 按钮\n");
            }
            if (result == JOptionPane.CANCEL_OPTION){
                jta.append("用户点击了 取消 按钮\n");
            }
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
        new ConfirmDialogTest().init();
    }
}
