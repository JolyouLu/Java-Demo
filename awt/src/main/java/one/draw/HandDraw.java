package one.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 13:58
 * @Description
 */
public class HandDraw {
    private Frame frame = new Frame("简单手绘程序");
    //定义画图区的宽度
    private final int AREA_WIDTH = 500;
    private final int AREA_HEIGHT = 400;

    //定义右键菜单
    private PopupMenu colorMenu = new PopupMenu();
    private MenuItem redItem = new MenuItem("红色");
    private MenuItem greenItem = new MenuItem("绿色");
    private MenuItem blueItem = new MenuItem("蓝色");

    //定义变量记录当前画笔颜色
    private Color forceColor = Color.BLACK;

    //创建BufferedImage位图对象
    BufferedImage image = new BufferedImage(AREA_WIDTH, AREA_HEIGHT, BufferedImage.TYPE_INT_RGB);

    //通过绘图呼气关联的Graphics对象
    Graphics g = image.getGraphics();

    //自定义绘图区域
    private class MyCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    }

    MyCanvas drawArea = new MyCanvas();

    //记录鼠标拖动过程中的上一个坐标
    private int preX = -1;
    private int preY = -1;

    public void init() {
        //组装视图，逻辑控制
        //右键菜单的事件
        ActionListener listener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                switch (actionCommand) {
                    case "红色":
                        forceColor = Color.RED;
                        break;
                    case "绿色":
                        forceColor = Color.GREEN;
                        break;
                    case "蓝色":
                        forceColor = Color.BLUE;
                        break;
                }
            }
        };
        redItem.addActionListener(listener);
        greenItem.addActionListener(listener);
        blueItem.addActionListener(listener);
        //菜单组成
        colorMenu.add(redItem);
        colorMenu.add(greenItem);
        colorMenu.add(blueItem);
        drawArea.add(colorMenu);
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //鼠标释放时调用，判断是否右键
                if (e.isPopupTrigger()) {
                    colorMenu.show(drawArea, e.getX(), e.getY());
                }
                //重置perX和perY
                preX = -1;
                preY = -1;
            }
        });
        //设置位图背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, AREA_WIDTH, AREA_HEIGHT);
        //通过监听鼠标移动完成线条绘制
        drawArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //单鼠标左键按下拖到时触发
                if (preX > 0 && preY > 0) {
                    g.setColor(forceColor);
                    //画线，2点确定一条直线
                    g.drawLine(preX, preY, e.getX(), e.getY());
                }
                //修正preX和preY的值
                preX = e.getX();
                preY = e.getY();
                //重绘组件
                drawArea.repaint();
            }
        });
        drawArea.setPreferredSize(new Dimension(AREA_WIDTH,AREA_HEIGHT));
        frame.add(drawArea);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HandDraw().init();
    }
}
