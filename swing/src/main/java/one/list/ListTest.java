package one.list;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 15:20
 * @Description
 */
public class ListTest {
    JFrame jf = new JFrame("列表框测试");
    String[] books = {"java自学宝典","轻量级javaEE企业应用实战","Android基础教程","Jquery实战教程","SpringBoot企业级开发"};
    //定义布局选择按钮所在的面板
    JPanel layoutPanel = new JPanel();
    ButtonGroup layoutGroup = new ButtonGroup();

    //定义选择模式按钮所在面板
    JPanel selectModePanel = new JPanel();
    ButtonGroup selectModeGroup = new ButtonGroup();

    JTextArea favorite = new JTextArea(4,40);

    //用一个字符串数组来创建一个JList对象
    JList<String> bookList;
    JComboBox<String> bookSelector;

    public void init(){
        //组装JList内容
        bookList = new JList<>(books);
        //组装按钮
        addBtnLayoutPanel("纵向滚动",JList.VERTICAL);
        addBtnLayoutPanel("纵向换行",JList.VERTICAL_WRAP);
        addBtnLayoutPanel("横向换行",JList.HORIZONTAL_WRAP);

        addBtnSelectModePanel("无限制",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addBtnSelectModePanel("单选",ListSelectionModel.SINGLE_SELECTION);
        addBtnSelectModePanel("单范围",ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        //对JList设置
        bookList.setVisibleRowCount(3);
        bookList.setSelectionInterval(2,4);
        bookList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //获取当前选择
                List<String> valuesList = bookList.getSelectedValuesList();
                //将选择内容设置到文本域中
                favorite.setText("");
                for (String s : valuesList) {
                    favorite.append(s+"\n");
                }
            }
        });

        Box bookListVBox = Box.createVerticalBox();
        bookListVBox.add(new JScrollPane(bookList));
        bookListVBox.add(layoutPanel);
        bookListVBox.add(selectModePanel);

        //组装JComboBox
        Vector<String> vector = new Vector<>();
        List<String> list = List.of("java自学宝典", "轻量级javaEE企业应用实战", "Android基础教程", "Jquery实战教程", "SpringBoot企业级开发");
        vector.addAll(list);
        bookSelector = new JComboBox<>(vector);

        bookSelector.setEditable(true);
        bookSelector.setMaximumRowCount(4);
        bookSelector.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //获取当前已经选中的条目，把内容设置到文本域中
                Object selectedItem = bookSelector.getSelectedItem();
                favorite.setText(selectedItem.toString());
            }
        });
        //组装顶部左右2部分
        Box topBox = Box.createHorizontalBox();
        topBox.add(bookListVBox);
        JPanel bookSelectPanel = new JPanel();
        bookSelectPanel.add(bookSelector);
        topBox.add(bookSelectPanel);

        //组装底部
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(new JLabel("你最喜欢的图书："),BorderLayout.NORTH);
        bottomPanel.add(favorite);

        //组装整体
        Box holeBox = Box.createVerticalBox();
        holeBox.add(topBox);
        holeBox.add(bottomPanel);

        jf.add(holeBox);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    //封装方法往layoutPanel中添加按钮
    public void addBtnLayoutPanel(String name,int layoutType){
        //设置标题边框
        layoutPanel.setBorder(new TitledBorder(new EtchedBorder(),"确定选项布局"));
        //创建单选按钮
        JRadioButton button = new JRadioButton(name);
        layoutPanel.add(button);
        //默认选中第一次添加的按钮
        if (layoutGroup.getButtonCount() == 0){
            button.setSelected(true);
        }
        layoutGroup.add(button);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookList.setLayoutOrientation(layoutType);
            }
        });
    }

    //封装方法往selectModePanel中添加按钮
    public void addBtnSelectModePanel(String name,int selectionModel){
        //设置标题边框
        selectModePanel.setBorder(new TitledBorder(new EtchedBorder(),"确定选择模式"));
        //创建单选按钮
        JRadioButton button = new JRadioButton(name);
        selectModePanel.add(button);
        //默认选中第一次添加的按钮
        if (selectModeGroup.getButtonCount() == 0){
            button.setSelected(true);
        }
        selectModeGroup.add(button);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookList.setSelectionMode(selectionModel);
            }
        });
    }

    public static void main(String[] args) {
        new ListTest().init();
    }

}
