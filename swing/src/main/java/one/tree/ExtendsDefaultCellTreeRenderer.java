package one.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 20:54
 * @Description
 */
public class ExtendsDefaultCellTreeRenderer {
    JFrame jf = new JFrame("定义树的节点");

    JTree tree;

    //初始化5个图标
    ImageIcon rootIcon = new ImageIcon("swing/src/main/resources/ExtendsDefaultCellTreeRenderer/库.png");
    ImageIcon dataBaseIcon = new ImageIcon("swing/src/main/resources/ExtendsDefaultCellTreeRenderer/数据库.png");
    ImageIcon tableIcon = new ImageIcon("swing/src/main/resources/ExtendsDefaultCellTreeRenderer/表格.png");
    ImageIcon columnIcon = new ImageIcon("swing/src/main/resources/ExtendsDefaultCellTreeRenderer/列表.png");
    ImageIcon indexIcon = new ImageIcon("swing/src/main/resources/ExtendsDefaultCellTreeRenderer/钥匙.png");

    //定义一个NodeData类，用于封装数据节点
    class NodeData{
        public ImageIcon icon;
        public String name;

        public NodeData(ImageIcon icon, String name) {
            this.icon = icon;
            this.name = name;
        }
    }

    //定义几个初始节点
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(new NodeData(rootIcon,"数据库导航"));
    DefaultMutableTreeNode salaryDb = new DefaultMutableTreeNode(new NodeData(dataBaseIcon,"公司工资数据库"));
    DefaultMutableTreeNode customerDb = new DefaultMutableTreeNode(new NodeData(dataBaseIcon,"公司客户数据库"));
    DefaultMutableTreeNode employee = new DefaultMutableTreeNode(new NodeData(tableIcon,"员工表"));
    DefaultMutableTreeNode attend = new DefaultMutableTreeNode(new NodeData(tableIcon,"考勤表"));
    DefaultMutableTreeNode concat = new DefaultMutableTreeNode(new NodeData(tableIcon,"联系方式表"));
    DefaultMutableTreeNode id = new DefaultMutableTreeNode(new NodeData(indexIcon,"员工ID"));
    DefaultMutableTreeNode name = new DefaultMutableTreeNode(new NodeData(columnIcon,"姓名"));
    DefaultMutableTreeNode gender = new DefaultMutableTreeNode(new NodeData(columnIcon,"性别"));


    public void init(){
        //初始化父子关系
        root.add(salaryDb);
        root.add(customerDb);

        salaryDb.add(employee);
        salaryDb.add(attend);

        customerDb.add(concat);

        concat.add(id);
        concat.add(name);
        concat.add(gender);
        //创建JTree对象
        tree = new JTree(root);
        //设置节点渲染器
        tree.setCellRenderer(new MyRenderer());
        jf.add(new JScrollPane(tree));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    //自定义类基础DefaultCellTreeRenderer，完成节点的绘制
    private class MyRenderer extends DefaultTreeCellRenderer{

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            NodeData nodeData = (NodeData) node.getUserObject();
            this.setText(nodeData.name);
            this.setIcon(nodeData.icon);
            return this;
        }
    }

    public static void main(String[] args) {
        new ExtendsDefaultCellTreeRenderer().init();
    }
}
