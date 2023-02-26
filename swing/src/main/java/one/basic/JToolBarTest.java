package one.basic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 18:41
 * @Description
 */
public class JToolBarTest {

    JFrame jf = new JFrame("测试工具条");
    JTextArea jta = new JTextArea(6, 35);

    //声明工具条
    JToolBar jToolBar = new JToolBar("播放工具条", SwingConstants.HORIZONTAL);
    //创建3个Action对象
    //name与icon，最终添加到工具条中，会当做图标与名称
    Action pre = new AbstractAction("上一曲", new ImageIcon("swing/src/main/resources/BorderTest/左播放.png")) {

        public void actionPerformed(ActionEvent e) {
            jta.append("上一曲\n");
        }
    };
    Action pause = new AbstractAction("暂停", new ImageIcon("swing/src/main/resources/BorderTest/视频播放时暂停.png")) {

        public void actionPerformed(ActionEvent e) {
            jta.append("暂停\n");
        }
    };
    Action next = new AbstractAction("下一曲", new ImageIcon("swing/src/main/resources/BorderTest/右播放.png")) {

        public void actionPerformed(ActionEvent e) {
            jta.append("下一曲\n");
        }
    };

    public void init() {
        JButton preBtn = new JButton(pre);
        JButton pauseBtn = new JButton(pause);
        JButton nextBtn = new JButton(next);

        jToolBar.add(preBtn);
        jToolBar.addSeparator();
        jToolBar.add(pauseBtn);
        jToolBar.addSeparator();
        jToolBar.add(nextBtn);

        //使工具条可以拖动
        jToolBar.setFloatable(true);
        //将组件放入jScrollPane组件就有滚动条了
        JScrollPane jScrollPane = new JScrollPane(jta);

        jf.add(jToolBar, BorderLayout.NORTH);
        jf.add(jScrollPane);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new JToolBarTest().init();
    }
}
