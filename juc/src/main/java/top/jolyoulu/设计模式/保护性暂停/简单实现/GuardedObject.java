package top.jolyoulu.设计模式.保护性暂停.简单实现;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 17:20
 * @Version 1.0
 */
class GuardedObject<T> {
    //结果
    private T response;

    //获取结果
    public T get() {
        synchronized (this) {
            //还没有结果
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    //获取结果，带超时
    public T get(long timeout) {
        synchronized (this) {
            long begin = System.currentTimeMillis(); //记录开始时间
            long passedTime = 0; //经历的时间

            //还没有结果
            while (response == null) {
                //计算这次wait需要多久时间，timeout减上一次经历的时间
                long waitTime = timeout - passedTime;
                //判断是否已经超时
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //记录这次wait共用了多久
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(T response) {
        synchronized (this) {
            //将结果赋值
            this.response = response;
            this.notifyAll();
        }
    }
}
