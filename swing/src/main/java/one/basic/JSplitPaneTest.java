package one.basic;

import one.container.Book;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:43
 * @Description
 */
public class JSplitPaneTest {
    Book[] books = {
            new Book("Java自学宝典", new ImageIcon("swing/src/main/resources/SplitPaneTest/Java自学宝典.png"), "《Java自学宝典》是2017年10月1日清华大学出版社出版的图书，作者是黑马程序员。"),
            new Book("Android基础教程", new ImageIcon("swing/src/main/resources/SplitPaneTest/Android基础教程.png"), "《Android基础教程》是2013年中国水利水电出版社出版的图书，作者是余平、张建华。")
    };

    JFrame jf = new JFrame("测试JSplitPane");

    JList<Book> bookList = new JList<>(books);
    JLabel bookCover = new JLabel();
    JTextArea bookDesc = new JTextArea();

    public void init() {
        //设定组件大小
        bookList.setPreferredSize(new Dimension(150, 400));
        bookCover.setPreferredSize(new Dimension(220, 270));
        bookDesc.setPreferredSize(new Dimension(220, 130));
        //为JList设置条目选择监听器
        bookList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //获取选中条目
                Book book = bookList.getSelectedValue();
                //把数据图片设置到bookCover
                bookCover.setIcon(book.getIcon());
                //把数据的描述写入bookDesc
                bookDesc.setText(book.getDesc());
            }
        });
        //组装视图
        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bookCover, new JScrollPane(bookDesc));
        left.setOneTouchExpandable(true); //设置一触即展
        JSplitPane hole = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,left,bookList);
        hole.setContinuousLayout(true); //支持连续布局
        jf.add(hole);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new JSplitPaneTest().init();
    }
}
