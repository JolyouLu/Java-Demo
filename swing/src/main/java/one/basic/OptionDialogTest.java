package one.basic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:43
 * @Description
 */
public class OptionDialogTest {
    JFrame jf = new JFrame("测试选项对话框");
    JTextArea jta = new JTextArea(6,30);

    //声明按钮
    JButton btn = new JButton(new AbstractAction("弹出选项对话框") {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showOptionDialog(jf, "选择Size", "选项对话框", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new String[]{"大号", "中号", "小号"}, "中号");
            switch (result){
                case 0:
                    jta.append("用户选择了大号");
                    break;
                case 1:
                    jta.append("用户选择了中号");
                    break;
                case 2:
                    jta.append("用户选择了小号");
                    break;
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
        new OptionDialogTest().init();
    }
}
