package one.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/2/26 12:54
 * @Description
 */
public class SimpleDraw {
    private final String RECT_SHAPE="rect";
    private final String OVAL_SHAPE="oval";

    private Frame frame = new Frame("这里测试绘图");

    Button btnReact = new Button("绘制矩形");
    Button btnOval = new Button("绘制椭圆");
    //定义一个变量记录当前是要绘制椭圆还是矩形
    private String shape = "";

    //自定义类型，基础Canvas
    private class MyCanvas extends Canvas{
        @Override
        public void paint(Graphics g) {
            //绘制不同的图形
            if (shape.equals(RECT_SHAPE)){
                //绘制矩形
                g.setColor(Color.BLACK); //设置当前画笔的颜色为黑色
                g.drawRect(100,100,100,100);
            }else if (shape.equals(OVAL_SHAPE)){
                //绘制椭圆
                g.setColor(Color.RED);
                g.drawOval(100,100,100,100);
            }
        }
    }

    //创建自定义的画布对象
    MyCanvas drawAre = new MyCanvas();

    public void init(){
        //点击事件
        btnReact.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //修改标记的值为rect
                shape = RECT_SHAPE;
                drawAre.repaint(); //重绘
            }
        });
        btnOval.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //修改标记的值为rect
                shape = OVAL_SHAPE;
                drawAre.repaint(); //重绘
            }
        });
        //组装视图
        Panel p = new Panel();
        p.add(btnReact);
        p.add(btnOval);
        frame.add(p,BorderLayout.SOUTH);
        //drawAre大小设置
        drawAre.setPreferredSize(new Dimension(300,300));
        frame.add(drawAre);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleDraw().init();
    }
}
