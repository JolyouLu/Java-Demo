package one.list;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 17:39
 * @Description
 */
public class ListCellRendererTest {
    JFrame mainWin = new JFrame("好友列表");

    String[] friends = {"李清照", "苏格拉底", "李白", "弄玉", "虎头"};

    JList friendList = new JList(friends);

    public void init() {
        //给JList设置ListCellRenderer对象，指定列表项绘制到组件
        friendList.setCellRenderer(new MyRenderer());

        mainWin.add(friendList);
        mainWin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWin.pack();
        mainWin.setVisible(true);
    }

    private class MyRenderer extends JPanel implements ListCellRenderer {

        private String name;
        private ImageIcon icon;

        private Color backGround; //记录背景色(文字颜色)
        private Color forceGround;//记录前景色


        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            //重置成员变量的值
            this.name = value.toString();
            this.icon = new ImageIcon("swing/src/main/resources/ListCellRendererTest/" + this.name + ".png");
            this.backGround = isSelected ? list.getSelectionBackground() : list.getBackground();
            this.forceGround = isSelected ? list.getSelectionForeground() : list.getForeground();
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(60, 80);
        }

        //绘制列表项的内容
        @Override
        public void paint(Graphics g) {
            int imageWidth = icon.getImage().getWidth(null);
            int imageHeight = icon.getImage().getHeight(null);
            //填充背景矩形
            g.setColor(backGround);
            g.fillRect(0,0,getWidth(),getHeight());
            //绘制头像
            g.drawImage(icon.getImage(), this.getWidth() / 2 - imageWidth / 2, 10, null);
            //绘制昵称
            g.setColor(forceGround);
            g.setFont(new Font("StSon", Font.BOLD, 18));
            g.drawString(this.name, this.getWidth() / 2 - this.name.length() * 20 / 2, 10 + imageHeight + 20);
        }
    }


    public static void main(String[] args) {
        new ListCellRendererTest().init();
    }
}
