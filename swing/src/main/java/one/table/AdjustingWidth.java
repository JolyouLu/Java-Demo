package one.table;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 23:21
 * @Description
 */
public class AdjustingWidth {
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
        JTable table = new JTable(data, titles);
        //设置选择模式
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); //默认没有限制
        TableColumn column_1 = table.getColumn(titles[0]); //获取列1
        column_1.setMinWidth(40);
        TableColumn column_3 = table.getColumn(titles[2]); //获取列2
        column_3.setMaxWidth(50);
        //设置列表宽
        jf.add(new JScrollPane(table));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new AdjustingWidth().init();
    }
}
