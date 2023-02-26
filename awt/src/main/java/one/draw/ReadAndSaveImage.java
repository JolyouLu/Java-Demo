package one.draw;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 14:26
 * @Description
 */
public class ReadAndSaveImage {
    private Frame frame = new Frame("图片查看器");
    //定义菜单组件
    MenuBar menuBar = new MenuBar();
    Menu menu = new Menu("文件");
    MenuItem open = new MenuItem("打开");
    MenuItem save = new MenuItem("另存为");
    //声明BufferedImage对象，记录本地内存的图片
    BufferedImage image;

    private class MyCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            g.drawImage(image,0,0,null);
        }
    }
    MyCanvas drawArea = new MyCanvas();

    public void init() {
        //事件编写
        open.addActionListener(e -> {
            //打开一个文件对话框
            FileDialog fileDialog = new FileDialog(frame,"打开图片",FileDialog.LOAD);
            fileDialog.setVisible(true);
            //获取用户选择的图片路径以及名称
            String dir = fileDialog.getDirectory();
            String fileName = fileDialog.getFile();
            try {
                image = ImageIO.read(new File(dir,fileName));
                drawArea.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        save.addActionListener(e -> {
            //打开文件对话框
            FileDialog fileDialog = new FileDialog(frame,"保存图片",FileDialog.SAVE);
            fileDialog.setVisible(true);
            //获取用户选择的图片路径以及名称
            String dir = fileDialog.getDirectory();
            String fileName = fileDialog.getFile();
            try {
                ImageIO.write(image,"JPEG",new File(dir+fileName));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        //添加组件
        menu.add(open);
        menu.add(save);
        menuBar.add(menu);
        frame.setMenuBar(menuBar);
        frame.add(drawArea);

        frame.setBounds(20, 200, 740, 508);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new ReadAndSaveImage().init();
    }
}
