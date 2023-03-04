package one.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.security.spec.RSAOtherPrimeInfo;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 20:38
 * @Description
 */
public class SimpleJTree {
    JFrame jf = new JFrame("简单树");

    public void init(){
        //创建DefaultMutableTreeNode
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("中国");
        DefaultMutableTreeNode guangDong = new DefaultMutableTreeNode("广东");
        DefaultMutableTreeNode guangXi = new DefaultMutableTreeNode("广西");
        DefaultMutableTreeNode foShan = new DefaultMutableTreeNode("佛山");
        DefaultMutableTreeNode shanTou = new DefaultMutableTreeNode("汕头");
        DefaultMutableTreeNode guiLin = new DefaultMutableTreeNode("桂林");
        DefaultMutableTreeNode nanNing = new DefaultMutableTreeNode("南宁");

        root.add(guangDong);
        root.add(guangXi);

        guangDong.add(foShan);
        guangDong.add(shanTou);

        guangXi.add(guiLin);
        guangXi.add(nanNing);

        //创建JTree对象
        JTree tree = new JTree(root);
        jf.add(tree);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleJTree().init();
    }
}
