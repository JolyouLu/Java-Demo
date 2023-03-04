package one.table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 23:21
 * @Description
 */
public class DefaultTableModelTest {
    JFrame jf = new JFrame("DefaultTableModel");

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
        DefaultTableModel model = new DefaultTableModel(dataV,titlesV);
        JTable jTable = new JTable(model);
        jf.add(jTable);

        //创建按钮
        JButton addRow = new JButton("添加一行");
        JButton addColum = new JButton("添加一列");
        JButton deleteRow = new JButton("删除一行");

        addRow.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{});
            }
        });
        addColum.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addColumn("职业");
            }
        });
        deleteRow.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jTable.getSelectedRow();
                model.removeRow(selectedRow);
            }
        });
        JPanel panel = new JPanel();
        panel.add(addRow);
        panel.add(addColum);
        panel.add(deleteRow);
        jf.add(panel,BorderLayout.SOUTH);


        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new DefaultTableModelTest().init();
    }
}
