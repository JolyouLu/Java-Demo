package top.jolyoulu.demo.dubborpc.netty;

import top.jolyoulu.demo.dubborpc.provider.HelloServiceImp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 13:00
 * @Version 1.0
 */
//服务器handler
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并且调用服务
        System.out.println("msg="+msg);
        //客户端在调用服务器的api时，我们需要定义一个协议
        //要求每次发送消息时都必须以某个字符串开头如"HelloService#hello#"
        if (msg.toString().startsWith("HelloService#hello#")){
            String result = new HelloServiceImp().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
