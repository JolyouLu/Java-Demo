package one.progress;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/4 0:05
 * @Description
 */
public class JProgressMonitorTest {
    Timer timer;
    public void init(){
        //创建进度对话框
        ProgressMonitor monitor = new ProgressMonitor(null, "等待任务完成", "已完成", 0, 100);
        SimulaterActivity simulaterActivity = new SimulaterActivity(100);
        new Thread(simulaterActivity).start();
        //设置定时任务
        timer = new Timer(200, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //读取当前任务量，修改进度
                int current = simulaterActivity.getCurrent();
                monitor.setProgress(current);
                //判断用户是否点击取消按钮，点击了需求停止任务关闭对话框
                if (monitor.isCanceled()){
                    timer.stop();
                    monitor.close();
                    System.exit(0);
                }
            }
        });
        timer.start();
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
        new JProgressMonitorTest().init();
    }
}
