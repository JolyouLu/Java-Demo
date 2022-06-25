package top.jolyoulu.cas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/25 10:32
 * @Version 1.0
 */
public class DecimalDemo {

    public static void main(String[] args) {
        DecimalAccountCas accountCas = new DecimalAccountCas(new BigDecimal(10000));
        DecimalAccount.demo(accountCas);
    }

    public interface DecimalAccount{
        //并发测速
        static void demo(DecimalAccount account){
            ArrayList<Thread> ts = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                ts.add(new Thread(() -> {
                    account.withdraw(BigDecimal.TEN);
                }));
            }
            ts.forEach(Thread::start);
            ts.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            System.out.println(account.getBalance());
        }

        //获取余额
        BigDecimal getBalance();

        //取款
        void withdraw(BigDecimal amount);
    }

    //CAS操作实现类
    static class DecimalAccountCas implements DecimalAccount{

        private AtomicReference<BigDecimal> balance;

        public DecimalAccountCas(BigDecimal balance) {
            this.balance = new AtomicReference<>(balance);
        }

        @Override
        public BigDecimal getBalance() {
            return balance.get();
        }

        @Override
        public void withdraw(BigDecimal amount) {
            while (true){
                BigDecimal prev = balance.get();
                BigDecimal next = prev.subtract(amount);
                if (balance.compareAndSet(prev,next)){
                    break;
                }
            }
        }
    }
}
