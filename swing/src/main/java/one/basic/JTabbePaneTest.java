package one.basic;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/3 23:46
 * @Description
 */
public class JTabbePaneTest {
    JFrame jf = new JFrame("测试JtabbedPane");

    JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);

    public void init(){
        //添加标签
        tabbedPane.addTab("用户管理",new JList<String>(new String[]{"用户1","用户2","用户3"}));
        tabbedPane.addTab("商品管理",new JList<String>(new String[]{"商品1","商品2","商品3"}));
        tabbedPane.addTab("订单管理",new JList<String>(new String[]{"订单1","订单2","订单3"}));
        //完成设置
        tabbedPane.setEnabledAt(0,false);
        tabbedPane.setSelectedIndex(1);
        //监听当前面板的选中情况
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                JOptionPane.showMessageDialog(jf,"当前选择了第"+(selectedIndex + 1)+"个标签");
            }
        });
        jf.add(tabbedPane);
        //设置窗口位置和大小
        jf.setBounds(400,400,400,400);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new JTabbePaneTest().init();
    }
}
