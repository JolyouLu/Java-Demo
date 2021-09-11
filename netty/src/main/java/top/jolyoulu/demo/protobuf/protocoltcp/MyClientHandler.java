package top.jolyoulu.demo.protobuf.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 16:38
 * @Version 1.0
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端循环发送10条数据
        for (int i = 0; i < 10; i++) {
            String msg = "今天天气冷，吃火锅";
            byte[] content = msg.getBytes(CharsetUtil.UTF_8);
            int length = msg.getBytes(CharsetUtil.UTF_8).length;
            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到消息");
        System.out.println("长度="+len);
        System.out.println("内容="+new String(content,CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
