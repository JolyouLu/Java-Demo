package top.jolyoulu.demo.protobuf.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 16:39
 * @Version 1.0
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收数据并且处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务端接收到消息如下");
        System.out.println("长度="+len);
        System.out.println("内容="+new String(content,CharsetUtil.UTF_8));
        System.out.println("");
        //回复客户端消息
        String respString = UUID.randomUUID().toString();
        int respLen = respString.getBytes(CharsetUtil.UTF_8).length;
        //构成协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(respLen);
        messageProtocol.setContent(respString.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
