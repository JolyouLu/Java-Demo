package one.table;

import javax.swing.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 23:21
 * @Description
 */
public class SimpleTable {
    JFrame jf = new JFrame("简单表格");

    //创建一维数组，表头
    Object[] titles = {"姓名", "年龄", "性别"};

    //创建二维数组，存储数据
    Object[][] data = {
            {"李清照", 29, "女"},
            {"苏格拉底", 15, "男"},
            {"李白", 22, "男"},
            {"弄玉", 43, "女"},
            {"虎头", 2, "男"}
    };

    public void init() {
        JTable jTable = new JTable(data, titles);
        jf.add(new JScrollPane(jTable));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleTable().init();
    }
}
