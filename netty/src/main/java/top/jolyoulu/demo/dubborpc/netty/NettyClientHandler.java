package top.jolyoulu.demo.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 13:13
 * @Version 1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    //上下文
    private ChannelHandlerContext context;
    //返回结果
    private String result;
    //客户端调用方法时，传入传输
    private String para;

    //连接成功后执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //把上下文保存下来，在其它方法时需要使用
        System.out.println("channelActive 被调用~~");
        context = ctx;
    }

    //收到客户端消息后执行
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead 被调用~~");
        result = msg.toString();
        //获取之前call中挂起的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器， => wait => 等待被幻想 => 返回结果
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call 被调用~~");
        context.writeAndFlush(para);
        //进行wait
        wait(); //等待channelRead得到结果后唤醒
        System.out.println("call 被调用~~");
        return result; //服务方返回的结果
    }

    void setPara(String para) {
        System.out.println("setPara 被调用~~");
        this.para = para;
    }
}
