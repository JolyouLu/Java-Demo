package one.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 20:54
 * @Description
 */
public class CustomerTreeNode {
    JFrame jf = new JFrame("定义树的节点");

    JTree tree;

    //定义几个初始节点
    DefaultMutableTreeNode friends = new DefaultMutableTreeNode("我的好友");
    DefaultMutableTreeNode qingzhao = new DefaultMutableTreeNode("李清照");
    DefaultMutableTreeNode suge = new DefaultMutableTreeNode("苏格拉底");
    DefaultMutableTreeNode libai = new DefaultMutableTreeNode("李白");
    DefaultMutableTreeNode nongyou = new DefaultMutableTreeNode("弄玉");
    DefaultMutableTreeNode hutou = new DefaultMutableTreeNode("虎头");


    public void init() {
        //初始化父子关系
        friends.add(qingzhao);
        friends.add(suge);
        friends.add(libai);
        friends.add(nongyou);
        friends.add(hutou);

        //创建JTree对象
        tree = new JTree(friends);
        //设置节点渲染器
        tree.setCellRenderer(new MyRenderer());
        jf.add(new JScrollPane(tree));

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    //自定义类基础DefaultCellTreeRenderer，完成节点的绘制
    private class MyRenderer extends JPanel implements TreeCellRenderer {

        private String name;
        private ImageIcon icon;

        private Color backGround; //记录背景色(文字颜色)
        private Color forceGround;//记录前景色

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            this.name = value.toString();
            this.icon = new ImageIcon("swing/src/main/resources/CustomerTreeNode/" + this.name + ".png");
            this.backGround = hasFocus ? new Color(144,200,255) : new Color(255,255,255);
            this.forceGround = hasFocus ? new Color(255,255,3) : new Color(0,0,0);
            return this;
        }

        //通过重写getPreferenceSize方法，指定当前Jpanel组件大小
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 80);
        }

        @Override
        public void paint(Graphics g) {
            //绘制组件
            int iconWidth = this.icon.getIconWidth();
            int iconHeight = this.icon.getIconHeight();
            //填充背景
            g.setColor(backGround);
            g.fillRect(0, 0, getWidth(), getHeight());
            //绘制头像
            g.drawImage(this.icon.getImage(), getWidth() / 2 - iconWidth / 2, 10, null);
            //绘制昵称
            g.setColor(forceGround);
            g.setFont(new Font("StSon", Font.BOLD, 18));
            g.drawString(this.name, this.getWidth() / 2 - this.name.length() * 20 / 2, 10 + iconHeight + 20);
        }
    }

    public static void main(String[] args) {
        new CustomerTreeNode().init();
    }
}
