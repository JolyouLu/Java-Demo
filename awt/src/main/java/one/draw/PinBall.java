package one.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 13:12
 * @Description
 */
public class PinBall {
    private Frame frame = new Frame("弹球小游戏");
    //设置桌面宽度
    private final int TABLE_WIDTH = 300;
    private final int TABLE_HEIGHT = 400;
    //设置球拍高度和宽度
    private final int RACKET_WIDTH = 60;
    private final int RACKET_HEIGHT = 20;
    //球的大小
    private final int BALL_SIZE = 16;
    //记录小球的坐标(每次刷新都会变化)
    private int ballX = 120;
    private int ballY = 20;
    //记录小球在x，y上的移动速度
    private int speedY = 10;
    private int speedX = 5;
    //记录球拍的坐标(每次刷新都会变化)
    private int racketX = 120;
    private final int racketY = 340;
    //标记游戏是否结束
    private boolean isOver = false;
    //声明一个定时器
    private Timer timer;

    //自定义画布
    private class MyCanvas extends Canvas {
        @Override
        public void paint(Graphics g) {
            //判断游戏是否结束
            if (isOver) {
                //游戏结束
                g.setColor(Color.BLUE);
                g.setFont(new Font("Times", Font.BOLD, 30));
                g.drawString("游戏结束！", 50, 200);
            } else {
                //游戏中
                // 绘制小球
                g.setColor(Color.RED);
                g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);
                //绘制球拍
                g.setColor(Color.PINK);
                g.fillRect(racketX, racketY, RACKET_WIDTH, RACKET_HEIGHT);
            }
        }
    }

    private MyCanvas drawArea = new MyCanvas();


    public void init() {
        //组装视图，游戏逻辑
        //监听球拍坐标变化
        KeyListener listener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //获取当前按键
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    // 按下左键，向左移
                    if (racketX > 0) {
                        racketX -= 10;
                    }
                }
                if (keyCode == KeyEvent.VK_RIGHT) {
                    // 按下右键，向右移
                    if (racketX < (TABLE_WIDTH - RACKET_WIDTH)) {
                        racketX += 10;
                    }
                }
            }
        };
        //非Frame和drawArea注册监听器
        frame.addKeyListener(listener);
        drawArea.addKeyListener(listener);
        //小球坐标的控制，使用定时器不断刷新
        ActionListener task = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //根据边界范围修正速度
                if (ballX <= 0 || ballX >= (TABLE_WIDTH - BALL_SIZE)) {
                    speedX = -speedX;
                }
                //如果小球碰到球拍，修正速度
                if (ballY <= 0 || (ballY > racketY - BALL_SIZE && ballX > racketX && ballX < racketX + RACKET_WIDTH)) {
                    speedY = -speedY;
                }
                //小球已经超出球拍范围游戏结束
                if (ballY > racketY - BALL_SIZE && (ballX < racketX || ballX > racketX + RACKET_WIDTH)) {
                    //停止定时器
                    timer.stop();
                    //修改游戏是否结束标记
                    isOver = true;
                    //重绘
                    drawArea.repaint();
                }
                //更新小球的坐标
                ballX += speedX;
                ballY += speedY;
                //重绘界面
                drawArea.repaint();
            }
        };
        timer = new Timer(100, task);
        timer.start();
        drawArea.setPreferredSize(new Dimension(TABLE_WIDTH,TABLE_HEIGHT));
        frame.add(drawArea);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new PinBall().init();
    }
}
