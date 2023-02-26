package one.basic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 19:22
 * @Description
 */
public class JFileChooseDemo {
    JFrame jf = new JFrame("测试JFileChooser");
    //创建菜单条
    JMenuBar jmb = new JMenuBar();
    //创建菜单
    JMenu jMenu = new JMenu("文件");
    JMenuItem open = new JMenuItem(new AbstractAction("打开") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showOpenDialog(jf);
            //获取用户选择的文件
            File file = fileChooser.getSelectedFile();
            //显示图片
            try {
                image = ImageIO.read(file);
                drawArea.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    });
    JMenuItem save = new JMenuItem(new AbstractAction("另存为") {
        @Override
        public void actionPerformed(ActionEvent e) {
            //显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showSaveDialog(jf);
            //获取用户选择的文件
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(image,"jpeg",file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    });

    BufferedImage image;
    //swing提供的组件，都使用了图像缓冲区技术
    private class MyCanvas extends JPanel{
        @Override
        public void paint(Graphics g) {
            //把图片绘制到组件上
            g.drawImage(image,0,0,null);
        }
    }
    MyCanvas drawArea = new MyCanvas();

    public void init(){
        jMenu.add(open);
        jMenu.add(save);
        jmb.add(jMenu);
        jf.setJMenuBar(jmb);

        drawArea.setPreferredSize(new Dimension(740,500));
        jf.add(drawArea);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new JFileChooseDemo().init();
    }
}
