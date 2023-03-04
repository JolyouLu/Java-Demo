package one.progress;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 0:05
 * @Description
 */
public class JProgressTest2 {
    JFrame jf = new JFrame("测试进度条");

    JCheckBox indeterminate = new JCheckBox("不确定进度");
    JCheckBox noBorder = new JCheckBox("不绘制边框");

    JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);

    //获取进度条内置的数据模型对象
    BoundedRangeModel model = bar.getModel();

    public void init(){
        //复选框的点击行为
        indeterminate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取是否选中状态
                if (indeterminate.isSelected()) {
                    bar.setIndeterminate(true);
                    bar.setStringPainted(false);
                }else {
                    bar.setIndeterminate(false);
                    bar.setStringPainted(true);
                }
            }
        });
        noBorder.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取是否选中状态
                if (noBorder.isSelected()) {
                    bar.setBorderPainted(false);
                }else {
                    bar.setBorderPainted(true);
                }
            }
        });
        Box vBox = Box.createVerticalBox();
        vBox.add(indeterminate);
        vBox.add(noBorder);
        //设置进度条属性
        bar.setStringPainted(true);
        bar.setBorderPainted(true);
        bar.setValue(50);
        //使用流式布局
        jf.setLayout(new FlowLayout());
        jf.add(vBox);
        jf.add(bar);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setVisible(true);
        //开启子线程模拟耗时操作
        SimulaterActivity simulaterActivity = new SimulaterActivity(bar.getMaximum());
        new Thread(simulaterActivity).start();
        //设置定时任务不断读取当前进度
        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int current = simulaterActivity.getCurrent();
                model.setValue(current);
            }
        });
        timer.start();
        //监听进度条每次变化，当进度条当前值等于任务中的值时表示任务完毕了，定时任务可以关闭
        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (model.getValue() == simulaterActivity.getAmount()) {
                    timer.stop();
                }
            }
        });
    }

    private class SimulaterActivity implements Runnable{
        //记录当前任务总量
        private int amount;

        //记录当前任务完成量
        private volatile int current;

        public SimulaterActivity(int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public int getCurrent() {
            return current;
        }

        @Override
        public void run() {
            while (current < amount){
                try {
                    Thread.currentThread().sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                current++;
            }
        }
    }

    public static void main(String[] args) {
        new JProgressTest2().init();
    }
}
