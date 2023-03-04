package one.list;

import one.model.NumberComboBoxListModel;
import one.model.NumberListModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 17:07
 * @Description
 */
public class ListModeTest {
    JFrame jf = new JFrame("测试ListModel");

    JList<BigDecimal> jList = new JList<>(new NumberListModel(new BigDecimal(1),new BigDecimal(21),new BigDecimal(2)));

    JComboBox<BigDecimal> jComboBox = new JComboBox<>(new NumberComboBoxListModel(new BigDecimal(0.1),new BigDecimal(1.2),new BigDecimal(0.1)));

    JLabel label = new JLabel("你选择的是：");
    JTextField jTextField = new JTextField(15);

    public void init(){
        //组装顶部
        jList.setVisibleRowCount(4);
        jList.setSelectionInterval(2,4);
        jList.setFixedCellWidth(90);
        jList.setFixedCellHeight(30);
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                List<BigDecimal> list = jList.getSelectedValuesList();
                jTextField.setText("");
                for (BigDecimal item : list) {
                    jTextField.setText(jTextField.getText()+item.toString()+",");
                }
            }
        });

        jComboBox.setMaximumRowCount(4);
        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object selectedItem = jComboBox.getSelectedItem();
                jTextField.setText(selectedItem.toString());
            }
        });

        Box hBox = Box.createHorizontalBox();
        hBox.add(new JScrollPane(jList));
        Panel tempPanel = new Panel();
        tempPanel.add(jComboBox);
        hBox.add(tempPanel);
        jf.add(hBox);
        //组装底部
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(label);
        bottomPanel.add(jTextField);
        jf.add(bottomPanel,BorderLayout.SOUTH);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new ListModeTest().init();
    }
}
