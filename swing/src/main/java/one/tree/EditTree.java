package one.tree;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 20:54
 * @Description
 */
public class EditTree {
    JFrame jf = new JFrame("可编辑结点的树");

    //创建DefaultMutableTreeNode
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("中国");
    DefaultMutableTreeNode guangDong = new DefaultMutableTreeNode("广东");
    DefaultMutableTreeNode guangXi = new DefaultMutableTreeNode("广西");
    DefaultMutableTreeNode foShan = new DefaultMutableTreeNode("佛山");
    DefaultMutableTreeNode shanTou = new DefaultMutableTreeNode("汕头");
    DefaultMutableTreeNode guiLin = new DefaultMutableTreeNode("桂林");
    DefaultMutableTreeNode nanNing = new DefaultMutableTreeNode("南宁");

    //定义按钮
    JButton addSiblingBtn = new JButton("添加兄弟节点");
    JButton addChildBtn = new JButton("添加子节点");
    JButton deleteBtn = new JButton("删除节点");
    JButton editBtn = new JButton("编辑当前节点");

    public void init(){
        //初始化父子关系
        guangDong.add(foShan);
        guangDong.add(shanTou);
        guangXi.add(guiLin);
        guangXi.add(nanNing);
        root.add(guangDong);
        root.add(guangXi);
        //创建JTree对象
        JTree tree = new JTree(root);
        //设置树可编辑
        tree.setEditable(true);
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        //处理添加
        addSiblingBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //添加兄弟节点逻辑
                //获取当前节点
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode == null){
                    return;
                }
                //获取当前节点，父节点
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();
                if (parentNode == null){
                    return;
                }
                //把新节点通过父节点添加
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("新节点");
                int index = parentNode.getIndex(selectedNode);
                model.insertNodeInto(newNode,parentNode,index);
                //显示行节点
                TreeNode[] pathToRoot = model.getPathToRoot(newNode);
                TreePath treePath = new TreePath(pathToRoot);
                tree.scrollPathToVisible(treePath);
                //重绘tree
                tree.updateUI();
            }
        });
        addChildBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //为选中结点添加子节点
                //获取选中节点
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode == null){
                    return;
                }
                //创建新节点
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("新节点");
                //把新节点添加到当前节点
                selectedNode.add(newNode);
                //显示新节点
                TreeNode[] pathToRoot = model.getPathToRoot(newNode);
                TreePath treePath = new TreePath(pathToRoot);
                tree.scrollPathToVisible(treePath);
                //重绘UI
                tree.updateUI();
            }
        });
        //处理删除
        deleteBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getParent() != null){
                    model.removeNodeFromParent(selectedNode);
                }
            }
        });
        //处理编辑
        editBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取当前选中节点的路径
                TreePath selectionPath = tree.getSelectionPath();
                //判断如果路径不为空着设置该路径的最后一个节点可编辑
                if (selectionPath != null){
                    tree.startEditingAtPath(selectionPath);
                }
            }
        });
        //组装视图
        jf.add(new JScrollPane(tree));
        JPanel panel = new JPanel();
        panel.add(addSiblingBtn);
        panel.add(addChildBtn);
        panel.add(deleteBtn);
        panel.add(editBtn);
        jf.add(panel, BorderLayout.SOUTH);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new EditTree().init();
    }
}
