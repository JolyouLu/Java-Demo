package one.basic;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 16:24
 * @Description
 */
public class BorderTest {
    JFrame jf = new JFrame("测试边框");

    public void init() {
        //组装视图
        //修改JFrame的布局改为GridLayout
        jf.setLayout(new GridLayout(2, 4));

        //使用不同JPanel组件，并且设置边框

        //BevelBorder
        Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.GREEN, Color.BLUE, Color.GRAY);
        jf.add(getJPanelWithBorder(border, "BevelBorder"));

        //LineBorder
        Border lineBorder = BorderFactory.createLineBorder(Color.ORANGE, 10);
        jf.add(getJPanelWithBorder(lineBorder, "LineBorder"));

        //EmptyBorder
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 5, 20, 30);
        jf.add(getJPanelWithBorder(emptyBorder, "EmptyBorder"));

        //EtchedBorder
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.GREEN);
        jf.add(getJPanelWithBorder(etchedBorder, "EtchedBorder"));

        //TitleBorder
        TitledBorder titledBorder = new TitledBorder(new LineBorder(Color.ORANGE, 10), "标题",TitledBorder.LEFT,TitledBorder.BOTTOM);
        jf.add(getJPanelWithBorder(titledBorder, "TitleBorder"));

        //MatteBorder
        MatteBorder matteBorder = new MatteBorder(10, 5, 20, 10, Color.GREEN);
        jf.add(getJPanelWithBorder(matteBorder, "MatteBorder"));

        //CompoundBorder
        CompoundBorder compoundBorder = new CompoundBorder(new LineBorder(Color.RED, 10), titledBorder);
        jf.add(getJPanelWithBorder(compoundBorder, "CompoundBorder"));

        //设置窗口最佳大小，设置窗口可见
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public JPanel getJPanelWithBorder(Border border, String content) {
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel(content));
        //设置边框
        jPanel.setBorder(border);
        return jPanel;
    }

    public static void main(String[] args) {
        new BorderTest().init();
    }
}
