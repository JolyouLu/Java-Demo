package one.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 23:21
 * @Description
 */
public class TableModelTest {
    JFrame jf = new JFrame("TableModel");

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

    private Vector titlesV = new Vector(); //存储标题
    private Vector<Vector> dataV = new Vector<>();

    public void init() {
        for (int i = 0; i < titles.length; i++) {
            titlesV.add(titles[i]);
        }
        for (int i = 0; i < data.length; i++) {
            Vector<Object> t = new Vector<>();
            for (int j = 0; j < data[i].length; j++) {
                t.add(data[i][j]);
            }
            dataV.add(t);
        }
        MyTableModel myTableModel = new MyTableModel();
        JTable jTable = new JTable(myTableModel);
        jf.add(new JScrollPane(jTable));

        JButton btn = new JButton("获取选中行数据");
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedColumn = jTable.getSelectedColumn();
                int selectedRow = jTable.getSelectedRow();
                System.out.println("当前选中行的索引："+selectedRow);
                System.out.println("当前选中列的索引："+selectedColumn);
                Object value = myTableModel.getValueAt(selectedRow, selectedColumn);
                System.out.println("当前选中行第一个第一个内容："+value);
            }
        });
        jf.add(btn, BorderLayout.SOUTH);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    private class MyTableModel extends AbstractTableModel{

        @Override
        public int getRowCount() {
            return dataV.size();
        }

        @Override
        public int getColumnCount() {
            return titlesV.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return dataV.get(rowIndex).get(columnIndex);
        }

        @Override
        public String getColumnName(int column) {
            return titlesV.get(column).toString();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
    }

    public static void main(String[] args) {
        new TableModelTest().init();
    }
}
