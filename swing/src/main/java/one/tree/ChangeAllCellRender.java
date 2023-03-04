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
public class ChangeAllCellRender {
    JFrame jf = new JFrame("改变所有节点外观");

    JTree tree;

    //创建DefaultMutableTreeNode
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("中国");
    DefaultMutableTreeNode guangDong = new DefaultMutableTreeNode("广东");
    DefaultMutableTreeNode guangXi = new DefaultMutableTreeNode("广西");
    DefaultMutableTreeNode foShan = new DefaultMutableTreeNode("佛山");
    DefaultMutableTreeNode shanTou = new DefaultMutableTreeNode("汕头");
    DefaultMutableTreeNode guiLin = new DefaultMutableTreeNode("桂林");
    DefaultMutableTreeNode nanNing = new DefaultMutableTreeNode("南宁");


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

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        //设置非选定节点的背景颜色
        renderer.setBackgroundNonSelectionColor(new Color(220,220,220));
        //设置选中节点的背景颜色
        renderer.setBackgroundSelectionColor(new Color(140,140,140));
        //设置选中状态下节点的边框颜色
        renderer.setBorderSelectionColor(Color.BLACK);
        //设置折叠状态下的非叶子节点的图标
        renderer.setClosedIcon(new ImageIcon("swing/src/main/resources/ChangeAllCellRender/close.png"));
        //设置节点文本字体
        renderer.setFont(new Font("StSong",Font.BOLD,16));
        //设置叶子节点图标
        renderer.setLeafIcon(new ImageIcon("swing/src/main/resources/ChangeAllCellRender/leaf.png"));
        //设置处于展开状态下的非叶子节点图标
        renderer.setOpenIcon(new ImageIcon("swing/src/main/resources/ChangeAllCellRender/open.png"));
        //设置绘制非选中状态下节点文本颜色
        renderer.setTextNonSelectionColor(new Color(255,0,0));
        //设置选中状态下节点的文本颜色
        renderer.setTextSelectionColor(new Color(0,0,255));

        //把节点控制器给树对象
        tree.setCellRenderer(renderer);

        jf.add(new JScrollPane(tree));
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new ChangeAllCellRender().init();
    }
}
