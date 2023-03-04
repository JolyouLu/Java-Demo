package one.list;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 17:24
 * @Description
 */
public class DefaultListModelTest {
    JFrame jf = new JFrame("测试DefaultListMode");

    JTextField bookName = new JTextField(20);
    JButton removeBtn = new JButton("删除选中图书");
    JButton addBtn = new JButton("添加指定图书");

    JList<String> bookList;
    DefaultListModel<String> model = new DefaultListModel<>();

    public void init(){
        //组装视图
        model.addElement("java自学宝典");
        model.addElement("轻量级javaEE企业应用实战");
        model.addElement("Android基础教程");
        model.addElement("Jquery实战教程");
        bookList = new JList<String>(model);

        //设置JList
        bookList.setVisibleRowCount(4);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jf.add(new JScrollPane(bookList));

        //组装底部
        addBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = bookName.getText();
                if (!name.trim().equals("")){
                    model.addElement(name);
                }
            }
        });
        removeBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = bookList.getSelectedIndex();
                if (selectedIndex >= 0){
                    model.remove(selectedIndex);
                }
            }
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(bookName);
        bottomPanel.add(addBtn);
        bottomPanel.add(removeBtn);
        jf.add(bottomPanel, BorderLayout.SOUTH);


        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new DefaultListModelTest().init();
    }
}
