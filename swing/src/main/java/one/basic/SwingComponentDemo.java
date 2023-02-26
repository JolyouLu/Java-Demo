package one.basic;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 15:13
 * @Description
 */
public class SwingComponentDemo {
    JFrame f = new JFrame("测试Swing基本组件");
    //声明菜单相关的组件
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("文件");
    JMenu editMenu = new JMenu("编辑");

    JMenuItem auto = new JMenuItem("自动换行");
    JMenuItem copy = new JMenuItem("复制",new ImageIcon("swing/src/main/resources/SwingComponentDemo/copy.png"));
    JMenuItem paste = new JMenuItem("粘贴",new ImageIcon("swing/src/main/resources/SwingComponentDemo/paste.png"));

    JMenu formatMenu = new JMenu("格式");
    JMenuItem comment = new JMenuItem("注释");
    JMenuItem cancelComment = new JMenuItem("取消注释");

    //声明文本域
    JTextArea ta = new JTextArea(8, 20);

    //声明列表框
    JList<String> colorList = new JList<String>(new String[]{"红色", "绿色", "蓝色"});

    //声明选择相关组件
    JComboBox<String> colorSelect = new JComboBox<>();

    ButtonGroup bg = new ButtonGroup();
    JRadioButton male = new JRadioButton("男", false);
    JRadioButton female = new JRadioButton("女", false);

    JCheckBox isMarried = new JCheckBox("是否已婚？", true);

    //声明底部组件
    JTextField tf = new JTextField(40);
    JButton ok = new JButton("确定",new ImageIcon("swing/src/main/resources/SwingComponentDemo/ok.png"));

    //声明右键菜单
    JPopupMenu jPopupMenu = new JPopupMenu();
    ButtonGroup poupButtonBg = new ButtonGroup();
    JRadioButtonMenuItem metalItem = new JRadioButtonMenuItem("Metal 风格");
    JRadioButtonMenuItem nimbusItem = new JRadioButtonMenuItem("Nimbus 风格");
    JRadioButtonMenuItem windowsItem = new JRadioButtonMenuItem("Windows 风格", true);
    JRadioButtonMenuItem windowsClassicItem = new JRadioButtonMenuItem("Windows 经典风格");
    JRadioButtonMenuItem motifItem = new JRadioButtonMenuItem("Motif 风格");

    public void init() {
        //组装视图
        //底部
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(tf);
        bottomPanel.add(ok);
        f.add(bottomPanel, BorderLayout.SOUTH);
        //组装选择相关组件
        JPanel selectPanel = new JPanel();
        colorSelect.addItem("红色");
        colorSelect.addItem("绿色");
        colorSelect.addItem("蓝色");
        selectPanel.add(colorSelect);
        bg.add(male);
        bg.add(female);
        selectPanel.add(male);
        selectPanel.add(female);
        selectPanel.add(isMarried);
        Box topLeft = Box.createVerticalBox();
        topLeft.add(ta);
        topLeft.add(selectPanel);
        //组装右边
        Box top = Box.createHorizontalBox();
        top.add(topLeft);
        top.add(colorList);
        f.add(top);
        //组装菜单
        formatMenu.add(comment);
        formatMenu.add(cancelComment);
        editMenu.add(auto);
        editMenu.addSeparator();
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(formatMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        f.setJMenuBar(menuBar);
        //组装右键菜单
        poupButtonBg.add(metalItem);
        poupButtonBg.add(nimbusItem);
        poupButtonBg.add(windowsItem);
        poupButtonBg.add(windowsClassicItem);
        poupButtonBg.add(motifItem);
        ActionListener listener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //当前选择什么风格
                String actionCommand = e.getActionCommand();
                try {
                    changeFlavor(actionCommand);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        metalItem.addActionListener(listener);
        nimbusItem.addActionListener(listener);
        windowsItem.addActionListener(listener);
        windowsClassicItem.addActionListener(listener);
        motifItem.addActionListener(listener);
        jPopupMenu.add(metalItem);
        jPopupMenu.add(nimbusItem);
        jPopupMenu.add(windowsItem);
        jPopupMenu.add(windowsClassicItem);
        jPopupMenu.add(motifItem);
        //设置右键菜单，不需要监听鼠标事件了
        ta.setComponentPopupMenu(jPopupMenu);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    private void changeFlavor(String command) throws Exception {
        switch (command) {
            case "Metal 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            case "Nimbus 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case "Windows 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                break;
            case "Windows 经典风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                break;
            case "Motif 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                break;
        }
        //刷新组件的外观
        SwingUtilities.updateComponentTreeUI(f.getContentPane());
        SwingUtilities.updateComponentTreeUI(menuBar);
        SwingUtilities.updateComponentTreeUI(jPopupMenu);
    }

    public static void main(String[] args) {
        new SwingComponentDemo().init();
    }
}
