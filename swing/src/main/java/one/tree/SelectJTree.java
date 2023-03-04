package one.tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 20:54
 * @Description
 */
public class SelectJTree {
    JFrame jf = new JFrame("监听树的选择事件");

    JTree tree;

    //创建DefaultMutableTreeNode
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("中国");
    DefaultMutableTreeNode guangDong = new DefaultMutableTreeNode("广东");
    DefaultMutableTreeNode guangXi = new DefaultMutableTreeNode("广西");
    DefaultMutableTreeNode foShan = new DefaultMutableTreeNode("佛山");
    DefaultMutableTreeNode shanTou = new DefaultMutableTreeNode("汕头");
    DefaultMutableTreeNode guiLin = new DefaultMutableTreeNode("桂林");
    DefaultMutableTreeNode nanNing = new DefaultMutableTreeNode("南宁");

    JTextArea eventTxt = new JTextArea(5,20);

    public void init(){
        //初始化父子关系
        guangDong.add(foShan);
        guangDong.add(shanTou);
        guangXi.add(guiLin);
        guangXi.add(nanNing);
        root.add(guangDong);
        root.add(guangXi);
        //创建JTree对象
        tree = new JTree(root);
        //设置选择模式
        TreeSelectionModel selectionModel = tree.getSelectionModel();
        selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        //设置监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                //把当前选中节点路径显示到文本域中
                TreePath newLeadSelectionPath = e.getNewLeadSelectionPath();
                eventTxt.append(newLeadSelectionPath.toString()+"\n");
            }
        });

        Box box = Box.createHorizontalBox();
        box.add(new JScrollPane(tree));
        box.add(new JScrollPane(eventTxt));

        jf.add(box);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new SelectJTree().init();
    }
}
